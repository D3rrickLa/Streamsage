import { EventEmitter, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SuggestionPackage } from '../models/domains/suggestion-package';

const restURL = '/api/v1/prompts'

// for DP injection
// this will throw an error if it isn't directly on top of a class
@Injectable({
  providedIn: 'root'
})
export class PromptService {
  constructor(private http: HttpClient) {}

  postPrompt(data: any): Observable<SuggestionPackage> {
    return this.http.post(restURL, data, {headers: {"Accept-Language": "en-CA"}});
  }

  onAIResponse = new EventEmitter<SuggestionPackage>();
}
