-- =============================================
-- STEP 2: Cinema Table Creation Script
-- SQL Server Database Script
-- Creating Cinema table and establishing relationship with Room
-- RUN THIS AFTER alter_room_add_cinema_id.sql
-- =============================================

-- Create Cinema Table
CREATE TABLE [dbo].[cinema](
    [id] [int] IDENTITY(1,1) NOT NULL,
    [name] [nvarchar](255) NOT NULL,
    [address] [nvarchar](500) NULL,
    [phone] [nvarchar](20) NULL,
    [email] [nvarchar](100) NULL,
    [description] [nvarchar](1000) NULL,
    [is_active] [int] NOT NULL DEFAULT(1),
    
    PRIMARY KEY CLUSTERED ([id] ASC)
    WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

-- Add Foreign Key Constraint for Room -> Cinema relationship
-- (Only if cinema_id column exists and constraint doesn't exist)
IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS 
           WHERE TABLE_NAME = 'room' AND COLUMN_NAME = 'cinema_id')
   AND NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE name = 'FK_room_cinema')
BEGIN
    ALTER TABLE [dbo].[room] 
    ADD CONSTRAINT [FK_room_cinema] 
    FOREIGN KEY([cinema_id]) REFERENCES [dbo].[cinema] ([id])
    ON DELETE CASCADE
    
    PRINT 'Added FK_room_cinema foreign key constraint'
END
ELSE
BEGIN
    IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS 
                   WHERE TABLE_NAME = 'room' AND COLUMN_NAME = 'cinema_id')
        PRINT 'Warning: cinema_id column does not exist in room table. Run alter_room_add_cinema_id.sql first.'
    ELSE
        PRINT 'FK_room_cinema constraint already exists'
END
GO

-- Add Check Constraints for data validation
ALTER TABLE [dbo].[cinema] 
ADD CONSTRAINT [CK_cinema_is_active] 
CHECK ([is_active] IN (0, 1))
GO

-- Add indexes for better performance
CREATE NONCLUSTERED INDEX [IX_cinema_name] ON [dbo].[cinema] ([name]) 
INCLUDE ([is_active])
GO

CREATE NONCLUSTERED INDEX [IX_cinema_is_active] ON [dbo].[cinema] ([is_active])
GO

-- Room table indexes are created in Step 1 (alter_room_add_cinema_id.sql)

-- Insert sample cinema data
INSERT INTO [dbo].[cinema] ([name], [address], [phone], [email], [description], [is_active])
VALUES 
    ('CGV Vincom Center', '72 Le Thanh Ton Street, Ben Nghe Ward, District 1, Ho Chi Minh City', '028-3822-3456', 'cgv.vincom@cgv.vn', 'Premium cinema complex in the heart of Ho Chi Minh City', 1),
    ('Lotte Cinema Diamond Plaza', '34 Le Duan Boulevard, Ben Nghe Ward, District 1, Ho Chi Minh City', '028-3825-7777', 'diamond@lottecinema.com.vn', 'Luxury cinema experience with state-of-the-art technology', 1),
    ('Galaxy Cinema Nguyen Du', '116 Nguyen Du Street, Ben Thanh Ward, District 1, Ho Chi Minh City', '028-3914-6666', 'nguyendu@galaxycine.vn', 'Modern cinema with comfortable seating and premium sound', 1)
GO

-- =============================================
-- Useful Queries for Testing
-- =============================================

-- View all cinemas with their room counts
SELECT 
    c.id,
    c.name AS cinema_name,
    c.address,
    c.phone,
    c.email,
    COUNT(r.id) AS total_rooms,
    SUM(CASE WHEN r.is_active = 1 THEN 1 ELSE 0 END) AS active_rooms,
    SUM(CASE WHEN r.is_active = 1 THEN r.capacity ELSE 0 END) AS total_capacity
FROM [dbo].[cinema] c
LEFT JOIN [dbo].[room] r ON c.id = r.cinema_id
WHERE c.is_active = 1
GROUP BY c.id, c.name, c.address, c.phone, c.email
ORDER BY c.name
GO

-- View all rooms with cinema details
SELECT 
    r.id,
    r.name AS room_name,
    r.capacity,
    r.room_type,
    r.row_count,
    r.seats_per_row,
    c.name AS cinema_name,
    c.address AS cinema_address
FROM [dbo].[room] r
INNER JOIN [dbo].[cinema] c ON r.cinema_id = c.id
WHERE r.is_active = 1 AND c.is_active = 1
ORDER BY c.name, r.name
GO

-- View rooms by type
SELECT 
    room_type,
    COUNT(*) AS room_count,
    AVG(capacity) AS avg_capacity,
    MIN(capacity) AS min_capacity,
    MAX(capacity) AS max_capacity
FROM [dbo].[room]
WHERE is_active = 1
GROUP BY room_type
ORDER BY room_count DESC
GO

-- Check if cinema_id column exists in room table
SELECT 
    TABLE_NAME,
    COLUMN_NAME,
    DATA_TYPE,
    IS_NULLABLE,
    CHARACTER_MAXIMUM_LENGTH
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'room' AND COLUMN_NAME = 'cinema_id'
GO 