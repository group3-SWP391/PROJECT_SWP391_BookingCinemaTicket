-- =============================================
-- STEP 1: ALTER Room Table Script
-- SQL Server Database Script
-- Adding missing columns to existing room table
-- RUN THIS FIRST before add_cinema_table.sql
-- =============================================

-- Add cinema_id column if it doesn't exist
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS 
               WHERE TABLE_NAME = 'room' AND COLUMN_NAME = 'cinema_id')
BEGIN
    ALTER TABLE [dbo].[room] 
    ADD [cinema_id] [int] NULL
    
    PRINT 'Added cinema_id column to room table'
END
ELSE
BEGIN
    PRINT 'cinema_id column already exists in room table'
END
GO

-- Add room_type column if it doesn't exist
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS 
               WHERE TABLE_NAME = 'room' AND COLUMN_NAME = 'room_type')
BEGIN
    ALTER TABLE [dbo].[room] 
    ADD [room_type] [nvarchar](50) NULL
    
    PRINT 'Added room_type column to room table'
END
ELSE
BEGIN
    PRINT 'room_type column already exists in room table'
END
GO

-- Add description column if it doesn't exist
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS 
               WHERE TABLE_NAME = 'room' AND COLUMN_NAME = 'description')
BEGIN
    ALTER TABLE [dbo].[room] 
    ADD [description] [nvarchar](500) NULL
    
    PRINT 'Added description column to room table'
END
ELSE
BEGIN
    PRINT 'description column already exists in room table'
END
GO

-- Add is_active column if it doesn't exist
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS 
               WHERE TABLE_NAME = 'room' AND COLUMN_NAME = 'is_active')
BEGIN
    ALTER TABLE [dbo].[room] 
    ADD [is_active] [int] NOT NULL DEFAULT(1)
    
    PRINT 'Added is_active column to room table'
END
ELSE
BEGIN
    PRINT 'is_active column already exists in room table'
END
GO

-- Add row_count column if it doesn't exist
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS 
               WHERE TABLE_NAME = 'room' AND COLUMN_NAME = 'row_count')
BEGIN
    ALTER TABLE [dbo].[room] 
    ADD [row_count] [int] NULL
    
    PRINT 'Added row_count column to room table'
END
ELSE
BEGIN
    PRINT 'row_count column already exists in room table'
END
GO

-- Add seats_per_row column if it doesn't exist
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS 
               WHERE TABLE_NAME = 'room' AND COLUMN_NAME = 'seats_per_row')
BEGIN
    ALTER TABLE [dbo].[room] 
    ADD [seats_per_row] [int] NULL
    
    PRINT 'Added seats_per_row column to room table'
END
ELSE
BEGIN
    PRINT 'seats_per_row column already exists in room table'
END
GO

-- Update existing rooms to have default values
-- Set default cinema_id = 1 for existing rooms
IF EXISTS (SELECT * FROM [dbo].[room] WHERE cinema_id IS NULL)
BEGIN
    UPDATE [dbo].[room] 
    SET cinema_id = 1
    WHERE cinema_id IS NULL
    
    PRINT 'Updated existing rooms with default cinema_id = 1'
END
GO

-- Set default room_type = 'STANDARD' for existing rooms
IF EXISTS (SELECT * FROM [dbo].[room] WHERE room_type IS NULL)
BEGIN
    UPDATE [dbo].[room] 
    SET room_type = 'STANDARD'
    WHERE room_type IS NULL
    
    PRINT 'Updated existing rooms with default room_type = STANDARD'
END
GO

-- Make cinema_id NOT NULL after updating existing data
IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS 
           WHERE TABLE_NAME = 'room' AND COLUMN_NAME = 'cinema_id' AND IS_NULLABLE = 'YES')
BEGIN
    ALTER TABLE [dbo].[room] 
    ALTER COLUMN [cinema_id] [int] NOT NULL
    
    PRINT 'Made cinema_id column NOT NULL'
END
GO

-- Note: Foreign Key relationship will be created in add_cinema_table.sql
-- after the cinema table is created

-- Add Check Constraints for room table if they don't exist
IF NOT EXISTS (SELECT * FROM sys.check_constraints WHERE name = 'CK_room_is_active')
BEGIN
    ALTER TABLE [dbo].[room] 
    ADD CONSTRAINT [CK_room_is_active] 
    CHECK ([is_active] IN (0, 1))
    
    PRINT 'Added CK_room_is_active check constraint'
END
GO

IF NOT EXISTS (SELECT * FROM sys.check_constraints WHERE name = 'CK_room_capacity')
BEGIN
    ALTER TABLE [dbo].[room] 
    ADD CONSTRAINT [CK_room_capacity] 
    CHECK ([capacity] > 0)
    
    PRINT 'Added CK_room_capacity check constraint'
END
GO

IF NOT EXISTS (SELECT * FROM sys.check_constraints WHERE name = 'CK_room_row_count')
BEGIN
    ALTER TABLE [dbo].[room] 
    ADD CONSTRAINT [CK_room_row_count] 
    CHECK ([row_count] IS NULL OR [row_count] > 0)
    
    PRINT 'Added CK_room_row_count check constraint'
END
GO

IF NOT EXISTS (SELECT * FROM sys.check_constraints WHERE name = 'CK_room_seats_per_row')
BEGIN
    ALTER TABLE [dbo].[room] 
    ADD CONSTRAINT [CK_room_seats_per_row] 
    CHECK ([seats_per_row] IS NULL OR [seats_per_row] > 0)
    
    PRINT 'Added CK_room_seats_per_row check constraint'
END
GO

-- Add indexes for better performance (if they don't exist)
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'IX_room_cinema_id')
BEGIN
    CREATE NONCLUSTERED INDEX [IX_room_cinema_id] ON [dbo].[room] ([cinema_id]) 
    INCLUDE ([name], [capacity], [room_type], [is_active])
    
    PRINT 'Added IX_room_cinema_id index'
END
GO

IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'IX_room_type_active')
BEGIN
    CREATE NONCLUSTERED INDEX [IX_room_type_active] ON [dbo].[room] ([room_type], [is_active])
    
    PRINT 'Added IX_room_type_active index'
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
ORDER BY ORDINAL_POSITION
GO

PRINT 'Room table update completed successfully!' 