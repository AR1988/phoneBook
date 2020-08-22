import { Component, OnInit } from '@angular/core';
import {UserService} from "../service/user.service";

@Component({
  selector: 'app-activate-email',
  templateUrl: './activate-email.component.html',
  styleUrls: ['./activate-email.component.css']
})
export class ActivateEmailComponent implements OnInit {

  constructor(private userService : UserService) { }

  ngOnInit(): void {
    this.userService.getTest().subscribe(value1 => console.log(value1));
  }

}
