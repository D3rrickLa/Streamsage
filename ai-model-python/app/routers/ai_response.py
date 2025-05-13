import re
import ollama

from fastapi import APIRouter, Request, Response
from fastapi.responses import JSONResponse
from app.models.request_entity import PromptRequestDTO
from app.util.rate_limit import limiter
ai_response_router = APIRouter()

ENDPOINT = "/api/v1"

@ai_response_router.get(f"{ENDPOINT}/")
@limiter.limit("30/minute")
async def heartbeat(request: Request):
    return {"message" : "Alive and well."}

@ai_response_router.post(f"{ENDPOINT}/generate")
@limiter.limit("10/minute")
async def generate_media(request: Request, body: PromptRequestDTO): 
    response = ollama.chat(
        model="mistral:7b",
        messages=[{"role": "user", "content": body.prompt}]  # Ensure correct format
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