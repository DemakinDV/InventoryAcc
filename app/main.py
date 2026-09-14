from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
import sqlite3
from typing import List, Optional
from pydantic import BaseModel
import os

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

DATABASE_PATH = "/app/data/database.db"

class Account(BaseModel):
    id: str
    login: str
    password: str
    isLoggedIn: bool
    createdAt: int

class InventoryItem(BaseModel):
    id: str
    name: str
    quantity: Optional[int] = None
    description: Optional[str] = None
    price: Optional[float] = None
    purchaseDate: Optional[int] = None
    saleDate: Optional[int] = None
    note: Optional[str] = None
    isInStock: bool
    accountId: str
    createdAt: int

def get_db_connection():
    conn = sqlite3.connect(DATABASE_PATH)
    conn.row_factory = sqlite3.Row
    return conn

@app.get("/")
async def root():
    return {"message": "SQLite API Server", "status": "running"}

@app.get("/accounts")
async def get_accounts():
    conn = get_db_connection()
    cursor = conn.cursor()
    cursor.execute("SELECT * FROM accounts")
    rows = cursor.fetchall()
    conn.close()
    return [dict(row) for row in rows]

@app.get("/accounts/{account_id}")
async def get_account(account_id: str):
    conn = get_db_connection()
    cursor = conn.cursor()
    cursor.execute("SELECT * FROM accounts WHERE id = ?", (account_id,))
    row = cursor.fetchone()
    conn.close()
    if row is None:
        raise HTTPException(status_code=404, detail="Account not found")
    return dict(row)

@app.get("/items")
async def get_items():
    conn = get_db_connection()
    cursor = conn.cursor()
    cursor.execute("SELECT * FROM inventory_items")
    rows = cursor.fetchall()
    conn.close()
    return [dict(row) for row in rows]

@app.get("/items/{account_id}")
async def get_items_by_account(account_id: str):
    conn = get_db_connection()
    cursor = conn.cursor()
    cursor.execute("SELECT * FROM inventory_items WHERE accountId = ?", (account_id,))
    rows = cursor.fetchall()
    conn.close()
    return [dict(row) for row in rows]

@app.post("/items")
async def create_item(item: InventoryItem):
    conn = get_db_connection()
    cursor = conn.cursor()
    cursor.execute("""
        INSERT INTO inventory_items 
        (id, name, quantity, description, price, purchaseDate, saleDate, note, isInStock, accountId, createdAt)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """, (item.id, item.name, item.quantity, item.description, item.price, 
          item.purchaseDate, item.saleDate, item.note, item.isInStock, item.accountId, item.createdAt))
    conn.commit()
    conn.close()
    return {"message": "Item created successfully", "id": item.id}

@app.put("/items/{item_id}")
async def update_item(item_id: str, item: InventoryItem):
    conn = get_db_connection()
    cursor = conn.cursor()
    cursor.execute("""
        UPDATE inventory_items 
        SET name = ?, quantity = ?, description = ?, price = ?, 
            purchaseDate = ?, saleDate = ?, note = ?, isInStock = ?
        WHERE id = ?
    """, (item.name, item.quantity, item.description, item.price, 
          item.purchaseDate, item.saleDate, item.note, item.isInStock, item_id))
    conn.commit()
    conn.close()
    return {"message": "Item updated successfully"}

@app.delete("/items/{item_id}")
async def delete_item(item_id: str):
    conn = get_db_connection()
    cursor = conn.cursor()
    cursor.execute("DELETE FROM inventory_items WHERE id = ?", (item_id,))
    conn.commit()
    conn.close()
    return {"message": "Item deleted successfully"}

@app.post("/login")
async def login(login: str, password: str):
    conn = get_db_connection()
    cursor = conn.cursor()
    cursor.execute("SELECT * FROM accounts WHERE login = ? AND password = ?", (login, password))
    row = cursor.fetchone()
    conn.close()
    if row is None:
        raise HTTPException(status_code=401, detail="Invalid credentials")
    return dict(row)

@app.post("/register")
async def register(account: Account):
    conn = get_db_connection()
    cursor = conn.cursor()
    cursor.execute("SELECT * FROM accounts WHERE login = ?", (account.login,))
    existing = cursor.fetchone()
    if existing:
        conn.close()
        raise HTTPException(status_code=400, detail="Login already exists")
    
    cursor.execute("""
        INSERT INTO accounts (id, login, password, isLoggedIn, createdAt)
        VALUES (?, ?, ?, ?, ?)
    """, (account.id, account.login, account.password, account.isLoggedIn, account.createdAt))
    conn.commit()
    conn.close()
    return {"message": "Account created successfully"}