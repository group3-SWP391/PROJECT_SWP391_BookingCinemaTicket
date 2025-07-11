ALTER TABLE room 
ADD vip_seats NVARCHAR(MAX);

-- Update existing rooms to have empty VIP seats by default
UPDATE room 
SET vip_seats = '' 
WHERE vip_seats IS NULL; 