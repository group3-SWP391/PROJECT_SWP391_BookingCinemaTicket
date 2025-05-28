CREATE TABLE movie (
    id INT IDENTITY(1,1) PRIMARY KEY,
    actors VARCHAR(255),
    categories VARCHAR(255),
    director VARCHAR(255),
    duration INT,
    is_showing INT,
    language VARCHAR(255),
    large_imageurl VARCHAR(1000),
    long_description VARCHAR(1000),
    name VARCHAR(255),
    rated VARCHAR(255),
    release_date DATE,
    short_description VARCHAR(500),
    small_imageurl VARCHAR(1000),
    trailerurl VARCHAR(1000)
);

CREATE TABLE [user] (
    id INT IDENTITY(1,1) PRIMARY KEY,
    full_name VARCHAR(255),
    password VARCHAR(255),
    phone VARCHAR(255),
    username VARCHAR(255)
    CONSTRAINT FK_user_role FOREIGN KEY (role_id) REFERENCES roles(id);
);

CREATE TABLE bill (
    id INT IDENTITY(1,1) PRIMARY KEY,
    created_time DATETIME,
    user_id INT,
    price FLOAT,
    CONSTRAINT FK_bill_user FOREIGN KEY (user_id) REFERENCES [user](id)
);

CREATE TABLE roles (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE reset_password_token (
    id INT IDENTITY(1,1) PRIMARY KEY,
    expiry_date DATETIME,
    token VARCHAR(255),
    user_id INT,
    CONSTRAINT FK_reset_token_user FOREIGN KEY (user_id) REFERENCES [user](id)
);

CREATE TABLE branch (
    id INT IDENTITY(1,1) PRIMARY KEY,
    dia_chi VARCHAR(255),
    imgurl VARCHAR(2000),
    name VARCHAR(255),
    phone_no VARCHAR(255)
);

CREATE TABLE room (
    id INT IDENTITY(1,1) PRIMARY KEY,
    capacity INT,
    imgurl VARCHAR(1000),
    name VARCHAR(255),
    total_area FLOAT,
    branch_id INT,
    CONSTRAINT FK_room_branch FOREIGN KEY (branch_id) REFERENCES branch(id)
);

CREATE TABLE seat (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name VARCHAR(255),
    room_id INT,
    is_active BIT,
    is_vip BIT,
    CONSTRAINT FK_seat_room FOREIGN KEY (room_id) REFERENCES room(id)
);

CREATE TABLE schedule (
    id INT IDENTITY(1,1) PRIMARY KEY,
    price FLOAT,
    start_date DATE,
    start_time TIME,
    branch_id INT,
    movie_id INT,
    room_id INT,
    CONSTRAINT FK_schedule_branch FOREIGN KEY (branch_id) REFERENCES branch(id),
    CONSTRAINT FK_schedule_movie FOREIGN KEY (movie_id) REFERENCES movie(id),
    CONSTRAINT FK_schedule_room FOREIGN KEY (room_id) REFERENCES room(id)
);

CREATE TABLE ticket (
    id INT IDENTITY(1,1) PRIMARY KEY,
    qr_imageurl VARCHAR(255),
    bill_id INT,
    schedule_id INT,
    seat_id INT,
    CONSTRAINT FK_ticket_bill FOREIGN KEY (bill_id) REFERENCES bill(id),
    CONSTRAINT FK_ticket_schedule FOREIGN KEY (schedule_id) REFERENCES schedule(id),
    CONSTRAINT FK_ticket_seat FOREIGN KEY (seat_id) REFERENCES seat(id)
);
