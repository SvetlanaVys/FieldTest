import {Component, OnInit} from '@angular/core';
import {ResponseService} from '../shared/services/response.service';
import {Response} from '../shared/models/response';
import {Field} from '../shared/models/field';
import {FieldService} from '../shared/services/field.service';

@Component({
  selector: 'app-response',
  templateUrl: './response.component.html',
  styleUrls: ['../fields/field.component.css'],
  providers: [ResponseService]
})
export class ResponseComponent implements OnInit {

  responses: Response[];
  fields: Field[];
  responsesArray:  Object = {};
  max: number;
  array: number[] = [];
  startPos: number;
  endPos: number;
  step: number;
  page: number;
  isAll: boolean;
  isReady: boolean;

  constructor(private serv: ResponseService, private servField: FieldService) {
    this.max = 0;
    this.startPos = 0;
    this.step = 2;
    this.page = 1;
    this.isReady = false;
    this.isAll = true;
  }

  ngOnInit(): void {
    this.loadResponses();
    this.loadFields();
  }

  // load responses
  loadResponses() {
    this.serv.getResponse().subscribe((data: Response[]) => {
      this.responses = data;
      this.createResponsesArray(data);
    });
  }

  // load fields
  loadFields() {
    this.servField.getFielts().subscribe((data: Field[]) => {
      this.fields = data;
    });
  }

  // create "matrix" of responses
  createResponsesArray(responses: Response[]) {
    for (const response of responses) {
      this.responsesArray[response.row] = new Object();
      this.array.push(response.row);
      this.startPos = this.array[0];
    }
    for (const response of responses) {
      this.responsesArray[response.row][response.field.id] = response.content;
      this.max = Math.max(this.max, this.array.length - 1);
    }
    this.isReady = true;
  }
}
