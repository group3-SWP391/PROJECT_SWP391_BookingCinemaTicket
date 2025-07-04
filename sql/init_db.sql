USE [SWP391]
GO
/****** Object:  Table [dbo].[actor]    Script Date: 04/07/2025 09:59:59 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[actor](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [nvarchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[advertising_contact_request]    Script Date: 04/07/2025 09:59:59 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[advertising_contact_request](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[company_address] [varchar](255) NOT NULL,
	[company_name] [varchar](255) NOT NULL,
	[created_at] [datetime2](6) NOT NULL,
	[email] [varchar](255) NOT NULL,
	[full_name] [varchar](255) NOT NULL,
	[notes] [varchar](255) NULL,
	[phone] [varchar](255) NOT NULL,
	[rental_date] [date] NOT NULL,
 CONSTRAINT [PK__advertis__3213E83F82325F6B] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[bill]    Script Date: 04/07/2025 09:59:59 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[bill](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[created_time] [datetime] NULL,
	[user_id] [int] NULL,
	[price] [float] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[branch]    Script Date: 04/07/2025 09:59:59 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[branch](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[location] [nvarchar](255) NULL,
	[img_url] [nvarchar](2000) NULL,
	[name] [nvarchar](255) NULL,
	[phone_no] [nvarchar](255) NULL,
	[description] [nchar](1000) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[cinema]    Script Date: 04/07/2025 09:59:59 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[cinema](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [nvarchar](255) NOT NULL,
	[address] [nvarchar](500) NULL,
	[phone] [nvarchar](20) NULL,
	[email] [nvarchar](100) NULL,
	[description] [nvarchar](1000) NULL,
	[is_active] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[contact_request]    Script Date: 04/07/2025 09:59:59 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[contact_request](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[created_at] [datetime2](6) NULL,
	[email] [varchar](255) NOT NULL,
	[full_name] [varchar](255) NOT NULL,
	[message] [varchar](2000) NOT NULL,
	[phone] [varchar](255) NOT NULL,
	[request_type] [varchar](255) NOT NULL,
 CONSTRAINT [PK__contact___3213E83F5DCAAD8C] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[director]    Script Date: 04/07/2025 09:59:59 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[director](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [nvarchar](255) NOT NULL,
	[description] [nvarchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[event]    Script Date: 04/07/2025 09:59:59 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[event](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [nvarchar](255) NOT NULL,
	[description] [nvarchar](255) NULL,
	[start_date] [datetime] NOT NULL,
	[end_date] [datetime] NOT NULL,
	[branch_id] [int] NULL,
	[movie_id] [int] NULL,
	[image_large_url] [nvarchar](1000) NULL,
	[status] [bit] NULL,
	[image_small_url] [nvarchar](1000) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[movie]    Script Date: 04/07/2025 09:59:59 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[movie](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[duration] [int] NULL,
	[is_showing] [int] NULL,
	[language] [nvarchar](255) NULL,
	[large_imageurl] [nvarchar](1000) NULL,
	[long_description] [nvarchar](1000) NULL,
	[name] [nvarchar](255) NULL,
	[rated] [nvarchar](255) NULL,
	[release_date] [date] NULL,
	[short_description] [nvarchar](500) NULL,
	[small_imageurl] [nvarchar](1000) NULL,
	[trailerurl_watch_link] [nvarchar](1000) NULL,
	[format] [nchar](10) NULL,
	[director_id] [int] NULL,
	[categories] [nvarchar](100) NULL,
	[views] [int] NULL,
	[end_date] [date] NULL,
	[rating_id] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[movie_actor]    Script Date: 04/07/2025 09:59:59 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[movie_actor](
	[movie_id] [int] NOT NULL,
	[actor_id] [int] NOT NULL,
	[name_in_movie] [nvarchar](255) NULL,
 CONSTRAINT [PK_movie_actor] PRIMARY KEY CLUSTERED 
(
	[movie_id] ASC,
	[actor_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[rating]    Script Date: 04/07/2025 09:59:59 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[rating](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [nvarchar](50) NOT NULL,
	[description] [nvarchar](255) NULL,
	[age_limit] [int] NULL,
	[is_active] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [UQ_rating_name] UNIQUE NONCLUSTERED 
(
	[name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[reset_password_token]    Script Date: 04/07/2025 09:59:59 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[reset_password_token](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[expiry_date] [datetime] NULL,
	[token] [nvarchar](255) NULL,
	[user_id] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[roles]    Script Date: 04/07/2025 09:59:59 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[roles](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [nvarchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[room]    Script Date: 04/07/2025 09:59:59 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[room](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[capacity] [int] NULL,
	[imgurl] [nvarchar](1000) NULL,
	[name] [nvarchar](255) NULL,
	[total_area] [float] NULL,
	[branch_id] [int] NULL,
	[cinema_id] [int] NOT NULL,
	[room_type] [nvarchar](50) NULL,
	[description] [nvarchar](500) NULL,
	[is_active] [int] NOT NULL,
	[row_count] [int] NULL,
	[seats_per_row] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[schedule]    Script Date: 04/07/2025 09:59:59 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[schedule](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[price] [float] NULL,
	[start_date] [date] NULL,
	[start_time] [time](7) NULL,
	[branch_id] [int] NULL,
	[movie_id] [int] NULL,
	[room_id] [int] NULL,
	[end_time] [time](7) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[seat]    Script Date: 04/07/2025 09:59:59 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[seat](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [nvarchar](255) NULL,
	[room_id] [int] NULL,
	[is_active] [bit] NULL,
	[is_vip] [bit] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ticket]    Script Date: 04/07/2025 09:59:59 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ticket](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[qr_imageurl] [nvarchar](255) NULL,
	[bill_id] [int] NULL,
	[schedule_id] [int] NULL,
	[seat_id] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[user]    Script Date: 04/07/2025 09:59:59 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[user](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[full_name] [nvarchar](255) NULL,
	[password] [nvarchar](255) NULL,
	[phone] [nvarchar](255) NULL,
	[username] [nvarchar](255) NULL,
	[role_id] [int] NULL,
	[email] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
ALTER TABLE [dbo].[cinema] ADD  DEFAULT ((1)) FOR [is_active]
GO
ALTER TABLE [dbo].[rating] ADD  DEFAULT ((1)) FOR [is_active]
GO
ALTER TABLE [dbo].[room] ADD  DEFAULT ((1)) FOR [is_active]
GO
ALTER TABLE [dbo].[bill]  WITH CHECK ADD  CONSTRAINT [FK_bill_user] FOREIGN KEY([user_id])
REFERENCES [dbo].[user] ([id])
GO
ALTER TABLE [dbo].[bill] CHECK CONSTRAINT [FK_bill_user]
GO
ALTER TABLE [dbo].[event]  WITH CHECK ADD  CONSTRAINT [FK_event_branch] FOREIGN KEY([branch_id])
REFERENCES [dbo].[branch] ([id])
GO
ALTER TABLE [dbo].[event] CHECK CONSTRAINT [FK_event_branch]
GO
ALTER TABLE [dbo].[event]  WITH CHECK ADD  CONSTRAINT [FK_event_movie] FOREIGN KEY([movie_id])
REFERENCES [dbo].[movie] ([id])
GO
ALTER TABLE [dbo].[event] CHECK CONSTRAINT [FK_event_movie]
GO
ALTER TABLE [dbo].[movie]  WITH CHECK ADD  CONSTRAINT [FK_movie_director] FOREIGN KEY([director_id])
REFERENCES [dbo].[director] ([id])
GO
ALTER TABLE [dbo].[movie] CHECK CONSTRAINT [FK_movie_director]
GO
ALTER TABLE [dbo].[movie]  WITH CHECK ADD  CONSTRAINT [FK_movie_rating] FOREIGN KEY([rating_id])
REFERENCES [dbo].[rating] ([id])
GO
ALTER TABLE [dbo].[movie] CHECK CONSTRAINT [FK_movie_rating]
GO
ALTER TABLE [dbo].[movie_actor]  WITH CHECK ADD  CONSTRAINT [FK_movie_actor_actor] FOREIGN KEY([actor_id])
REFERENCES [dbo].[actor] ([id])
GO
ALTER TABLE [dbo].[movie_actor] CHECK CONSTRAINT [FK_movie_actor_actor]
GO
ALTER TABLE [dbo].[movie_actor]  WITH CHECK ADD  CONSTRAINT [FK_movie_actor_movie] FOREIGN KEY([movie_id])
REFERENCES [dbo].[movie] ([id])
GO
ALTER TABLE [dbo].[movie_actor] CHECK CONSTRAINT [FK_movie_actor_movie]
GO
ALTER TABLE [dbo].[reset_password_token]  WITH CHECK ADD  CONSTRAINT [FK_reset_token_user] FOREIGN KEY([user_id])
REFERENCES [dbo].[user] ([id])
GO
ALTER TABLE [dbo].[reset_password_token] CHECK CONSTRAINT [FK_reset_token_user]
GO
ALTER TABLE [dbo].[room]  WITH CHECK ADD  CONSTRAINT [FK_room_branch] FOREIGN KEY([branch_id])
REFERENCES [dbo].[branch] ([id])
GO
ALTER TABLE [dbo].[room] CHECK CONSTRAINT [FK_room_branch]
GO
ALTER TABLE [dbo].[room]  WITH CHECK ADD  CONSTRAINT [FK_room_cinema] FOREIGN KEY([cinema_id])
REFERENCES [dbo].[cinema] ([id])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[room] CHECK CONSTRAINT [FK_room_cinema]
GO
ALTER TABLE [dbo].[schedule]  WITH CHECK ADD  CONSTRAINT [FK_schedule_branch] FOREIGN KEY([branch_id])
REFERENCES [dbo].[branch] ([id])
GO
ALTER TABLE [dbo].[schedule] CHECK CONSTRAINT [FK_schedule_branch]
GO
ALTER TABLE [dbo].[schedule]  WITH CHECK ADD  CONSTRAINT [FK_schedule_movie] FOREIGN KEY([movie_id])
REFERENCES [dbo].[movie] ([id])
GO
ALTER TABLE [dbo].[schedule] CHECK CONSTRAINT [FK_schedule_movie]
GO
ALTER TABLE [dbo].[schedule]  WITH CHECK ADD  CONSTRAINT [FK_schedule_room] FOREIGN KEY([room_id])
REFERENCES [dbo].[room] ([id])
GO
ALTER TABLE [dbo].[schedule] CHECK CONSTRAINT [FK_schedule_room]
GO
ALTER TABLE [dbo].[seat]  WITH CHECK ADD  CONSTRAINT [FK_seat_room] FOREIGN KEY([room_id])
REFERENCES [dbo].[room] ([id])
GO
ALTER TABLE [dbo].[seat] CHECK CONSTRAINT [FK_seat_room]
GO
ALTER TABLE [dbo].[ticket]  WITH CHECK ADD  CONSTRAINT [FK_ticket_bill] FOREIGN KEY([bill_id])
REFERENCES [dbo].[bill] ([id])
GO
ALTER TABLE [dbo].[ticket] CHECK CONSTRAINT [FK_ticket_bill]
GO
ALTER TABLE [dbo].[ticket]  WITH CHECK ADD  CONSTRAINT [FK_ticket_schedule] FOREIGN KEY([schedule_id])
REFERENCES [dbo].[schedule] ([id])
GO
ALTER TABLE [dbo].[ticket] CHECK CONSTRAINT [FK_ticket_schedule]
GO
ALTER TABLE [dbo].[ticket]  WITH CHECK ADD  CONSTRAINT [FK_ticket_seat] FOREIGN KEY([seat_id])
REFERENCES [dbo].[seat] ([id])
GO
ALTER TABLE [dbo].[ticket] CHECK CONSTRAINT [FK_ticket_seat]
GO
ALTER TABLE [dbo].[user]  WITH CHECK ADD  CONSTRAINT [FK_user_role] FOREIGN KEY([role_id])
REFERENCES [dbo].[roles] ([id])
GO
ALTER TABLE [dbo].[user] CHECK CONSTRAINT [FK_user_role]
GO
ALTER TABLE [dbo].[cinema]  WITH CHECK ADD  CONSTRAINT [CK_cinema_is_active] CHECK  (([is_active]=(1) OR [is_active]=(0)))
GO
ALTER TABLE [dbo].[cinema] CHECK CONSTRAINT [CK_cinema_is_active]
GO
ALTER TABLE [dbo].[rating]  WITH CHECK ADD  CONSTRAINT [CK_rating_is_active] CHECK  (([is_active]=(1) OR [is_active]=(0)))
GO
ALTER TABLE [dbo].[rating] CHECK CONSTRAINT [CK_rating_is_active]
GO
ALTER TABLE [dbo].[room]  WITH CHECK ADD  CONSTRAINT [CK_room_capacity] CHECK  (([capacity]>(0)))
GO
ALTER TABLE [dbo].[room] CHECK CONSTRAINT [CK_room_capacity]
GO
ALTER TABLE [dbo].[room]  WITH CHECK ADD  CONSTRAINT [CK_room_is_active] CHECK  (([is_active]=(1) OR [is_active]=(0)))
GO
ALTER TABLE [dbo].[room] CHECK CONSTRAINT [CK_room_is_active]
GO
ALTER TABLE [dbo].[room]  WITH CHECK ADD  CONSTRAINT [CK_room_row_count] CHECK  (([row_count] IS NULL OR [row_count]>(0)))
GO
ALTER TABLE [dbo].[room] CHECK CONSTRAINT [CK_room_row_count]
GO
ALTER TABLE [dbo].[room]  WITH CHECK ADD  CONSTRAINT [CK_room_seats_per_row] CHECK  (([seats_per_row] IS NULL OR [seats_per_row]>(0)))
GO
ALTER TABLE [dbo].[room] CHECK CONSTRAINT [CK_room_seats_per_row]
GO
