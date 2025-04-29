import asyncio
import ollama
from fastapi import FastAPI
from pydantic import BaseModel
from fastapi.responses import JSONResponse
import uvicorn

app = FastAPI()

class GenerateRequest(BaseModel):
    prompt: str

@app.post("/generate")
async def generate_media(request: GenerateRequest): 
    response = ollama.chat(
        model="mistral:7b",
        messages=[{"role": "user", "content": request.prompt}]  # Ensure correct format
    )
    message_text = response.get("message", {}).get("content", "")  
    return JSONResponse(content={"response": message_text})

async def main() -> None:
    try:
        config = uvicorn.Config("ai_model_api:app", host="localhost", port=50001, log_config=None)
        await uvicorn.Server(config=config).serve()
    except KeyboardInterrupt:
        print("Server stopping by user")
    except Exception as e:
        print(f"Error in main loop: {e}")
    finally:
        pass

if __name__ == "__main__":
    asyncio.run(main())