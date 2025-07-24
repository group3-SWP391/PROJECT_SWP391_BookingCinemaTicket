-- Migration script to add location_detail column to branch table
-- Run this script manually to update the database schema


-- Add location_detail column to branch table
ALTER TABLE [branch] 
ADD [location_detail] NVARCHAR(500) NULL;
GO

-- Verify the column was added successfully
SELECT COLUMN_NAME, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH, IS_NULLABLE
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'branch' AND COLUMN_NAME = 'location_detail';
GO

PRINT 'Column location_detail has been successfully added to branch table';

-- Optional: Add some sample data for existing branches (uncomment if needed)
/*
UPDATE [branch] 
SET [location_detail] = 'Số 123, Tòa nhà ABC, Quận Cầu Giấy'
WHERE [location] = 'Hà Nội';

UPDATE [branch] 
SET [location_detail] = 'Số 456, Tòa nhà DEF, Quận Hải Châu'
WHERE [location] = 'Đà Nẵng';
*/