import asyncio
import re
import signal
import sys
from time import sleep
import ollama
from fastapi import FastAPI
from pydantic import BaseModel
from fastapi.responses import JSONResponse
import uvicorn

app = FastAPI()

ENDPOINT = "/api/v1"

class GenerateRequest(BaseModel):
    prompt: str


@app.get(f"{ENDPOINT}/")
async def heartbeat():
    return {"message" : "Alive and well."}

@app.post(f"{ENDPOINT}/generate")
async def generate_media(request: GenerateRequest): 
    response = ollama.chat(
        model="mistral:7b",
        messages=[{"role": "user", "content": request.prompt}]  # Ensure correct format
    )
    ai_response = response.get("message", {}).get("content", "") 
    message_text = parse_ai_response(ai_response)
    return JSONResponse(content={"message": message_text})

def parse_ai_response(response: str):
    cleaned_response = response.strip('{}"').replace('\\', '')
    movie_names = re.findall(r'\d+\.\s*"?([^"\n]+)"?', cleaned_response)

    if not movie_names: # handles cases where user ask for 1 movie
        if len([cleaned_response]) == 1:
            return [cleaned_response[2:]]
    
    return movie_names




async def main() -> None:
    try:
        config = uvicorn.Config("ai_model_api:app", host="localhost", port=50001, reload=True)
        server = uvicorn.Server(config=config)
        print("Starting Server")
        await server.serve()  # Start server
    except KeyboardInterrupt:
        print("Server stopping by user")
    except Exception as e:
        print(f"Error in main loop: {e}")
    finally:
        print("Shutting down server...")
        await server.shutdown()  # Ensure the proper shutdown call
        await sleep(1)  # Small delay to allow clean exit

if __name__ == "__main__":
    asyncio.run(main())