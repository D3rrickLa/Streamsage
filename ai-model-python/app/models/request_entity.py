from pydantic import BaseModel

class PromptRequestDTO(BaseModel):
    prompt: str
