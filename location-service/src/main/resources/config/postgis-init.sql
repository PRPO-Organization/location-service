CREATE TABLE IF NOT EXISTS user_locations (
                                              id SERIAL PRIMARY KEY,
                                              user_id VARCHAR(255) NOT NULL,
                                              geom geometry(Point, 4326),
                                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Add any additional columns here
                                              name VARCHAR(255),
                                              description TEXT
);

-- Create spatial index for better performance
CREATE INDEX IF NOT EXISTS idx_user_locations_geom
    ON user_locations USING GIST(geom);

-- Create index on user_id
CREATE INDEX IF NOT EXISTS idx_user_locations_user_id
    ON user_locations(user_id);
