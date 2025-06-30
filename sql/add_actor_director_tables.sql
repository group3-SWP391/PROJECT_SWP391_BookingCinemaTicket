-- Create Actor table
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[actor]') AND type in (N'U'))
BEGIN
    CREATE TABLE actor (
        id INT IDENTITY(1,1) PRIMARY KEY,
        name NVARCHAR(255) NOT NULL
    )
END;

-- Create Director table
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[director]') AND type in (N'U'))
BEGIN
    CREATE TABLE director (
        id INT IDENTITY(1,1) PRIMARY KEY,
        name NVARCHAR(255) NOT NULL,
        description NVARCHAR(MAX)
    )
END;

-- Create Movie table
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[movie]') AND type in (N'U'))
BEGIN
    CREATE TABLE movie (
        id INT IDENTITY(1,1) PRIMARY KEY,
        categories NVARCHAR(255),
        duration INT,
        is_showing INT,
        language NVARCHAR(255),
        large_imageurl NVARCHAR(255),
        long_description NVARCHAR(1000),
        name NVARCHAR(255),
        rated NVARCHAR(255),
        rating_id INT,
        release_date DATE,
        short_description NVARCHAR(500),
        small_imageurl NVARCHAR(255),
        trailerurl_watch_link NVARCHAR(255),
        format NVARCHAR(10),
        director_id INT,
        views INT,
        end_date DATE,
        CONSTRAINT FK_movie_director FOREIGN KEY (director_id) REFERENCES director(id),
        CONSTRAINT FK_movie_rating FOREIGN KEY (rating_id) REFERENCES rating(id)
    )
END;

-- Add director_id column to Movie table if it doesn't exist
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'[dbo].[movie]') AND name = 'director_id')
BEGIN
    ALTER TABLE movie
    ADD director_id INT;
END;

-- Add end_date column to Movie table if it doesn't exist
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'[dbo].[movie]') AND name = 'end_date')
BEGIN
    ALTER TABLE movie
    ADD end_date DATE;
END;

-- Add format column to Movie table if it doesn't exist
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'[dbo].[movie]') AND name = 'format')
BEGIN
    ALTER TABLE movie
    ADD format NVARCHAR(10);
END;

-- Add trailerurl_watch_link column to Movie table if it doesn't exist
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'[dbo].[movie]') AND name = 'trailerurl_watch_link')
BEGIN
    ALTER TABLE movie
    ADD trailerurl_watch_link NVARCHAR(255);
END;

-- Add views column to Movie table if it doesn't exist
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'[dbo].[movie]') AND name = 'views')
BEGIN
    ALTER TABLE movie
    ADD views INT DEFAULT 0;
END;

-- Add foreign key constraint if it doesn't exist
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'FK_movie_director') AND parent_object_id = OBJECT_ID(N'movie'))
BEGIN
    ALTER TABLE movie
    ADD CONSTRAINT FK_movie_director FOREIGN KEY (director_id) REFERENCES director(id);
END;

-- Create MovieActor table (junction table for many-to-many relationship)
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[movie_actor]') AND type in (N'U'))
BEGIN
    CREATE TABLE movie_actor (
        movie_id INT,
        actor_id INT,
        name_in_movie NVARCHAR(255),
        CONSTRAINT PK_movie_actor PRIMARY KEY (movie_id, actor_id),
        CONSTRAINT FK_movie_actor_movie FOREIGN KEY (movie_id) REFERENCES movie(id),
        CONSTRAINT FK_movie_actor_actor FOREIGN KEY (actor_id) REFERENCES actor(id)
    )
END;

-- Add indexes if they don't exist
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_movie_actor_movie' AND object_id = OBJECT_ID('movie_actor'))
BEGIN
    CREATE INDEX idx_movie_actor_movie ON movie_actor(movie_id);
END;

IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_movie_actor_actor' AND object_id = OBJECT_ID('movie_actor'))
BEGIN
    CREATE INDEX idx_movie_actor_actor ON movie_actor(actor_id);
END;

-- Add index for director_id in Movie table
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_movie_director' AND object_id = OBJECT_ID('movie'))
BEGIN
    CREATE INDEX idx_movie_director ON movie(director_id);
END;

-- Add indexes for foreign keys in Movie table
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_movie_rating' AND object_id = OBJECT_ID('movie'))
BEGIN
    CREATE INDEX idx_movie_rating ON movie(rating_id);
END; 