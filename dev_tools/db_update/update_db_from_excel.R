rm(list=ls())
library(RSQLite)
library(openxlsx)
library(dplyr)
library(tibble)

setwd(dirname(rstudioapi::getActiveDocumentContext()$path))
setwd("..");setwd("..")
db_path <- "app/src/main/assets/database/ingredient_database.db"
ingredients_path <- "dev_tools/db_update/ingredients.xlsx"

conn <- RSQLite::dbConnect(RSQLite::SQLite(), db_path)
dbListTables(conn)
# dbGetQuery(conn, "SELECT * FROM ingredient_table")

# cols <- ingredients[,4:ncol(ingredients)]
concatenate_styles <- function(cols){
  styles_string <- rep("", nrow(cols))
  for(i in 1:ncol(cols)){
    styles_string <- paste0(styles_string,
                            if_else(is.na(cols[,i, drop=T]),"", paste0(cols[,i, drop=T],";")))
  }
  return(styles_string)
}


ingredients <- read.xlsx(ingredients_path) %>%
  as_tibble() 
ingredients$styles_string <- concatenate_styles(ingredients[,4:ncol(ingredients)])

ingredients <- 
  ingredients %>%
  select(name, type, styles_string,  max_use_per_plan) %>%
  mutate(is_required=0,
         is_forbidden=0,
         id=1:nrow(ingredients)) 

dbExecute(conn, "DROP TABLE ingredients")
# dbExecute(conn, "DROP TABLE temp")
dbWriteTable(conn, name="temp", value=ingredients, overwrite=TRUE)
dbExecute(conn, 
          "CREATE TABLE [ingredients] (
          id INT PRIMARY KEY NOT NULL,
          name TEXT NOT NULL, 
          type TEXT NOT NULL, 
          styles_string TEXT, 
          max_use_per_plan INTEGER NOT NULL,
          is_required INTEGER NOT NULL,
          is_forbidden INTEGER NOT NULL);") # Room does not support boolean. Integer will be converted to boolean in java
dbExecute(conn, 
          "INSERT INTO ingredients (
          id,
          name,
          type,
          styles_string,
          max_use_per_plan,
          is_required,
          is_forbidden
          )
          SELECT 
          id,
          name,
          type,
          styles_string,
          max_use_per_plan,
          is_required,
          is_forbidden
          FROM temp;")
dbExecute(conn,          
          "DROP TABLE temp;")

dbDisconnect(conn)
