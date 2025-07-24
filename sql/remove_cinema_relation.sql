-- =============================================
-- Remove Cinema Relationship Script
-- SQL Server Database Script
-- Removing relationship between Room and Cinema tables
-- =============================================

USE [cinema_booking]
GO

-- Step 1: Drop foreign key constraint if it exists
IF EXISTS (SELECT * FROM sys.foreign_keys 
           WHERE name = 'FK_room_cinema' AND parent_object_id = OBJECT_ID('room'))
BEGIN
    ALTER TABLE [dbo].[room] DROP CONSTRAINT [FK_room_cinema]
    PRINT 'Dropped FK_room_cinema foreign key constraint'
END
GO

-- Step 2: Drop index on cinema_id if it exists
IF EXISTS (SELECT * FROM sys.indexes WHERE name = 'IX_room_cinema_id')
BEGIN
    DROP INDEX [IX_room_cinema_id] ON [dbo].[room]
    PRINT 'Dropped IX_room_cinema_id index'
END
GO

-- Step 3: Add branch_id column if it doesn't exist
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS 
               WHERE TABLE_NAME = 'room' AND COLUMN_NAME = 'branch_id')
BEGIN
    ALTER TABLE [dbo].[room] 
    ADD [branch_id] [int] NULL
    PRINT 'Added branch_id column to room table'
END
GO

-- Step 4: Create index on branch_id
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'IX_room_branch_id')
BEGIN
    CREATE NONCLUSTERED INDEX [IX_room_branch_id] ON [dbo].[room] ([branch_id]) 
    INCLUDE ([name], [capacity], [room_type], [is_active])
    PRINT 'Added IX_room_branch_id index'
END
GO

-- Step 5: Drop cinema_id column
IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS 
           WHERE TABLE_NAME = 'room' AND COLUMN_NAME = 'cinema_id')
BEGIN
    -- First make it nullable if it's not
    ALTER TABLE [dbo].[room] ALTER COLUMN [cinema_id] [int] NULL
    
    -- Then drop the column
    ALTER TABLE [dbo].[room] DROP COLUMN [cinema_id]
    PRINT 'Dropped cinema_id column from room table'
END
GO

-- Step 6: Drop cinema table if it exists
IF EXISTS (SELECT * FROM sys.tables WHERE name = 'cinema')
BEGIN
    DROP TABLE [dbo].[cinema]
    PRINT 'Dropped cinema table'
END
GO

-- Show final room table structure
SELECT 
    COLUMN_NAME,
    DATA_TYPE,
    IS_NULLABLE,
    CHARACTER_MAXIMUM_LENGTH,
    COLUMN_DEFAULT
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'room'
ORDER BY ORDINAL_POSITION;
GO 