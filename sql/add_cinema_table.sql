-- =============================================
-- STEP 2: Cinema Table Creation Script
-- SQL Server Database Script
-- Creating Cinema table and establishing relationship with Room
-- RUN THIS AFTER alter_room_add_cinema_id.sql
-- =============================================

-
INSERT INTO [dbo].[cinema] ([name], [address], [phone], [email], [description], [is_active])
VALUES 
    ('CGV Vincom Center', '72 Le Thanh Ton Street, Ben Nghe Ward, District 1, Ho Chi Minh City', '028-3822-3456', 'cgv.vincom@cgv.vn', 'Premium cinema complex in the heart of Ho Chi Minh City', 1),
    ('Lotte Cinema Diamond Plaza', '34 Le Duan Boulevard, Ben Nghe Ward, District 1, Ho Chi Minh City', '028-3825-7777', 'diamond@lottecinema.com.vn', 'Luxury cinema experience with state-of-the-art technology', 1),
    ('Galaxy Cinema Nguyen Du', '116 Nguyen Du Street, Ben Thanh Ward, District 1, Ho Chi Minh City', '028-3914-6666', 'nguyendu@galaxycine.vn', 'Modern cinema with comfortable seating and premium sound', 1)
GO

