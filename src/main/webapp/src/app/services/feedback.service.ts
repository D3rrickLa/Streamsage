import { HttpClient } from '@angular/common/http';
import { EventEmitter, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Feedback } from '../models/feedback';

const restURL = '/api/v1/feedbacks'
@Injectable({
  providedIn: 'root'
})
export class FeedbackService {

  constructor(private http: HttpClient) { }

  sendFeedback(data: any): Observable<Feedback> {
    return this.http.post(restURL, data, {withCredentials: true})
  }

  getFeedback(): Observable<Feedback[]> {
    return this.http.get<Feedback[]>(restURL+"/users");
  }

  onFeedbackResponse = new EventEmitter<Feedback>();
}
