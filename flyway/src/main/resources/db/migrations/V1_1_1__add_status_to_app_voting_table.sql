ALTER TABLE app_rating
ALTER COLUMN user_id SET NOT NULL;

ALTER TABLE app_rating
ADD COLUMN status VARCHAR(17);

UPDATE app_rating
SET status = 'FEEDBACK_SENT';

ALTER TABLE app_rating
ALTER COLUMN status SET NOT NULL;