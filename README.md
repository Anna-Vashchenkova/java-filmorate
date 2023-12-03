# java-filmorate

Template repository for Filmorate project.

[схема БД](./diagramms/db_scheme.drawio.png)

(https://)Найти топ N самых популярных фильмов

```sql
SELECT f.name AS name,
    COUNT(l.user_id) AS likes
FROM film AS f
JOIN likes AS l ON f.id = l.film_id
GROUP BY f.name
ORDER BY likes DESC
LIMIT N;
```

Вывести топ N опулярных жанров

```sql
SELECT genre.genre, count(l.user_id) as cnt
FROM film_genres as genre
JOIN film AS f on genre.film_id = f.id
JOIN likes l on f.id = l.film_id
GROUP BY genre.genre
ORDER BY cnt DESC
LIMIT N;
```
