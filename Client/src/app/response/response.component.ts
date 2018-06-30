import {Component, OnInit} from '@angular/core';
import {ResponseService} from '../shared/services/response.service';

@Component({
  selector: 'app-response',
  templateUrl: './response.component.html',
  styleUrls: ['../fields/field.component.css'],
  providers: [ResponseService]
})
export class ResponseComponent implements OnInit {

  // responses: Response[];
  // editField: Response;
  constructor(private serv: ResponseService) {
  }


  ngOnInit(): void {
    this.loadResponses();
  }


  loadResponses() {
    // this.serv.getFielts().subscribe((data: Field[]) => {
    //   this.fields = data;
    // });
  }

  create() {
    // this.editField = new Field (0, 'title', 'Single line text', true, true);
    // this.serv.createFields(this.editField).subscribe((data: Field[]) => {
    //   this.fields = data;
    //   this.loadResponses();
    // });
  }
}
