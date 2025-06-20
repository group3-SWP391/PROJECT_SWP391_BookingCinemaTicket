-- =============================================
-- Rating Table Creation Script
-- SQL Server Database Script
-- Creating Rating table and updating Movie table
-- =============================================

-- Create Rating Table
CREATE TABLE [dbo].[rating](
    [id] [int] IDENTITY(1,1) NOT NULL,
    [name] [nvarchar](50) NOT NULL,
    [description] [nvarchar](255) NULL,
    [age_limit] [int] NULL,
    [is_active] [int] NOT NULL DEFAULT(1),
    
    PRIMARY KEY CLUSTERED ([id] ASC)
    WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

-- Add Check Constraints for data validation
ALTER TABLE [dbo].[rating] 
ADD CONSTRAINT [CK_rating_is_active] 
CHECK ([is_active] IN (0, 1))
GO

ALTER TABLE [dbo].[rating] 
ADD CONSTRAINT [UQ_rating_name] 
UNIQUE ([name])
GO

-- Add indexes for better performance
CREATE NONCLUSTERED INDEX [IX_rating_name] ON [dbo].[rating] ([name]) 
INCLUDE ([is_active])
GO

CREATE NONCLUSTERED INDEX [IX_rating_is_active] ON [dbo].[rating] ([is_active])
GO

-- Insert sample rating data
INSERT INTO [dbo].[rating] ([name], [description], [age_limit], [is_active])
VALUES 
    ('G', 'General Audiences - All ages admitted', 0, 1),
    ('PG', 'Parental Guidance Suggested - Some material may not be suitable for children', 0, 1),
    ('PG-13', 'Parents Strongly Cautioned - Some material may be inappropriate for children under 13', 13, 1),
    ('R', 'Restricted - Under 17 requires accompanying parent or adult guardian', 17, 1),
    ('NC-17', 'Adults Only - No one 17 and under admitted', 18, 1),
    ('NR', 'Not Rated - Content has not been rated', 0, 1),
    ('UR', 'Unrated - Director''s cut or extended version', 0, 1)
GO

-- Add rating_id column to movie table if it doesn't exist
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS 
               WHERE TABLE_NAME = 'movie' AND COLUMN_NAME = 'rating_id')
BEGIN
    ALTER TABLE [dbo].[movie] 
    ADD [rating_id] [int] NULL
    
    PRINT 'Added rating_id column to movie table'
END
ELSE
BEGIN
    PRINT 'rating_id column already exists in movie table'
END
GO

-- Migrate existing rated data to rating_id
UPDATE m 
SET m.rating_id = r.id
FROM [dbo].[movie] m
INNER JOIN [dbo].[rating] r ON UPPER(LTRIM(RTRIM(m.rated))) = UPPER(r.name)
WHERE m.rating_id IS NULL AND m.rated IS NOT NULL AND m.rated != ''
GO

-- Add Foreign Key Constraint for Movie -> Rating relationship
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE name = 'FK_movie_rating')
BEGIN
    ALTER TABLE [dbo].[movie] 
    ADD CONSTRAINT [FK_movie_rating] 
    FOREIGN KEY([rating_id]) REFERENCES [dbo].[rating] ([id])
    
    PRINT 'Added FK_movie_rating foreign key constraint'
END
ELSE
BEGIN
    PRINT 'FK_movie_rating constraint already exists'
END
GO

-- Add index on movie.rating_id for better performance
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'IX_movie_rating_id')
BEGIN
    CREATE NONCLUSTERED INDEX [IX_movie_rating_id] ON [dbo].[movie] ([rating_id])
    PRINT 'Added index on movie.rating_id'
END
ELSE
BEGIN
    PRINT 'Index IX_movie_rating_id already exists'
END
GO

-- =============================================
-- Useful Queries for Testing
-- =============================================

-- View all ratings
SELECT 
    r.id,
    r.name,
    r.description,
    r.age_limit,
    r.is_active,
    COUNT(m.id) AS movies_count
FROM [dbo].[rating] r
LEFT JOIN [dbo].[movie] m ON r.id = m.rating_id
WHERE r.is_active = 1
GROUP BY r.id, r.name, r.description, r.age_limit, r.is_active
ORDER BY r.age_limit, r.name
GO

-- View all movies with their ratings
SELECT 
    m.id,
    m.name AS movie_name,
    r.name AS rating_name,
    r.description AS rating_description,
    r.age_limit,
    m.rated AS old_rated_field
FROM [dbo].[movie] m
LEFT JOIN [dbo].[rating] r ON m.rating_id = r.id
ORDER BY m.name
GO

-- Check for movies without rating_id that have rated field
SELECT 
    m.id,
    m.name,
    m.rated,
    m.rating_id
FROM [dbo].[movie] m
WHERE m.rating_id IS NULL AND (m.rated IS NOT NULL AND m.rated != '')
GO

-- View rating distribution
SELECT 
    r.name AS rating,
    r.description,
    COUNT(m.id) AS movie_count,
    CAST(COUNT(m.id) * 100.0 / SUM(COUNT(m.id)) OVER() AS DECIMAL(5,2)) AS percentage
FROM [dbo].[rating] r
LEFT JOIN [dbo].[movie] m ON r.id = m.rating_id
WHERE r.is_active = 1
GROUP BY r.id, r.name, r.description
ORDER BY movie_count DESC
GO 