@echo off
echo ========================================
echo   Перезапуск Docker контейнера
echo ========================================
echo.

cd /d "%~dp0"

echo [1/5] Остановка контейнера...
docker-compose down
echo.

echo [2/5] Пересборка образа...
docker-compose build --no-cache
echo.

echo [3/5] Запуск контейнера...
docker-compose up -d
echo.

echo [4/5] Проверка статуса...
docker-compose ps
echo.

echo [5/5] Логи сервера:
docker-compose logs --tail=20
echo.

echo ========================================
echo   Готово! Сервер запущен на порту 8000
echo   http://localhost:8000/
echo ========================================

pause