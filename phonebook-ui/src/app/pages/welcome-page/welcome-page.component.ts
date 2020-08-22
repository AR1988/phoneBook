import {Component, OnInit} from '@angular/core';
import {ContactService} from "../../service/contact.service";

@Component({
  selector: 'app-welcome-page',
  templateUrl: './welcome-page.component.html',
  styleUrls: ['./welcome-page.component.css']
})
export class WelcomePageComponent implements OnInit {

  constructor(public contactService: ContactService) {
  }

  ngOnInit(): void {

  }

  onClickRemove(id: number) {
    this.contactService.removeContact(id);
  }
}
