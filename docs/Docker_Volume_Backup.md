🅰️ Case 1: Copy Docker Volume (Most Correct Way)
Step 1: Identify volume
docker volume ls


Example:

local   influencenet_postgres_data

Step 2: Create backup archive
docker run --rm \
-v influencenet_postgres_data:/data \
-v $(pwd):/backup \
alpine \
tar czf /backup/postgres_backup.tar.gz /data


✔ Creates postgres_backup.tar.gz
✔ Contains all DB data

Step 3: Copy to another PC

Use scp / USB / Google Drive / Git LFS:

scp postgres_backup.tar.gz user@other-pc:/home/user/

Step 4: Restore on new PC
docker volume create influencenet_postgres_data

docker run --rm \
-v influencenet_postgres_data:/data \
-v $(pwd):/backup \
alpine \
tar xzf /backup/postgres_backup.tar.gz -C /


Now start your containers — data is preserved ✅