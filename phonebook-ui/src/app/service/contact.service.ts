import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Contact} from "./contact";
import {Observable} from "rxjs";


@Injectable({
  providedIn: 'root'
})
export class ContactService {
  contacts: Observable<Contact[]>;
  private readonly contactPath = '/api/contact/all';

  constructor(private http: HttpClient) {
  }

  getAll(): Observable<Contact[]> {
    if (!this.contacts) {
      this.reload();
    }
    return this.contacts;
  }

  private reload(): void {
    this.contacts = this.http.get<Contact[]>(this.contactPath);
  }

  getContacts() {
    return this.http.get<Contact>(`${this.contactPath}`);
  }

  removeContact(id: number) {
    return this.http.delete(`${this.contactPath}/${id}`);
  }
}
