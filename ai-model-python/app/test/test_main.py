import asyncio
from multiprocessing import Process
from fastapi.testclient import TestClient
import httpx
import pytest
import uvicorn
from app.main import app


client = TestClient(app)
client

def test_ai_response_heartbeat():
    """Tests if the api is responding"""
    response = client.get("/api/v1/")
    assert response.status_code == 200
    assert response.json() == {"message" : "Alive and well."}


def test_ai_response_generate_media():
    """Tests if the AI model is responding and providing an output"""
    response = client.post("/api/v1/generate",
                           json={"prompt": "hello world"}
                           )
    assert  response.status_code == 200
    assert response.json() != None



    