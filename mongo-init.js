MONGO_ROOT_USERNAME="root"
MONGO_ROOT_PASSWORD="pass123"
MONGO_DB_NAME="git_repository_db"

db = db.getSiblingDB("admin");
db.createUser(
    {
        user: mongo_root_username,
        pwd: mongo_root_password,
        roles: [{ role: "readWrite", db: mongo_db_name } ]
    }
);