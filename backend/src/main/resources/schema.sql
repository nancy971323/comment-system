-- 用戶表
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- 留言表
CREATE TABLE IF NOT EXISTS comments (
    id SERIAL PRIMARY KEY,
    content VARCHAR(500) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    user_id INT NOT NULL,
    parent_id INT,
    likes INT NOT NULL DEFAULT 0,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (parent_id) REFERENCES comments(id)
);