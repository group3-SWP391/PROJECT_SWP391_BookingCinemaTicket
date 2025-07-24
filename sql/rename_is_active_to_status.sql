-- Migration script to rename column from is_active to status in user table
-- Run this script manually to update the database schema


-- Rename column from is_active to status in [user] table
EXEC sp_rename '[user].is_active', 'status', 'COLUMN';
GO

-- Verify the column rename was successful
SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'user' AND COLUMN_NAME = 'status';
GO

PRINT 'Column is_active has been successfully renamed to status in [user] table';