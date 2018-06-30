import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {ResponseService} from '../shared/services/response.service';
import {Field} from '../shared/models/field';
import {FieldService} from '../shared/services/field.service';
import {Option} from '../shared/models/option';
import {Response} from '../shared/models/response';

@Component({
  selector: 'app-create-response',
  templateUrl: './create-response.component.html',
  styleUrls: ['../active-user/active-user.component.css'],
  providers: [ResponseService]
})
export class CreateResponseComponent implements OnInit {

  test: string;

  isAnswer: boolean;
  isReady: boolean;
  fields: Field[];
  fieldList: Object = {};
  fieldOptions: Object = {};
  options: Option[];
  responses: Response[];
  response: Response;
  type: Object = {
    'Single line text' : 'text',
    'Multiline text'   : 'textarea',
    'Radio button'     : 'radio',
    'Checkbox'         : 'checkbox',
    'Combobox'         : 'option',
    'Date'             : 'date-local'
  };

  constructor(private serv: FieldService, private servResp: ResponseService, public router: Router) {
    this.isAnswer = false;
    this.isReady = false;
  }

  ngOnInit(): void {
    this.loadFields();
    this.loadOptions();
  }

  // load fields
  loadFields() {
    this.serv.getFielts().subscribe((data: Field[]) => {
      this.fields = data;
      this.fieldListGen();
    });

  }

  loadOptions() {
    this.serv.getAllOptions().subscribe((data: Option[]) => {
        this.options = data;
        this.fieldOption();
        this.isReady = true;
    });
  }

  fieldOption() {
    for (const option of this.options) {
      this.fieldOptions[option.field.label] = [];
    }
    for (const option of this.options) {
      this.fieldOptions[option.field.label].push(option.name);
    }
  }

  fieldListGen() {
    for (const field of this.fields) {
      this.fieldList[field.label] = '';
    }
  }

  create() {
    // this.servResp.getResponse().subscribe((data: Response[]) => {
    //     this.responses = data;
    //   for (const response of this.responses) {
    //     alert(response);
    //   }
    // });

    // for (const field of this.fields) {
    //   this.response = new Response(0, this.fieldList[field.label], field, 1);
    //   this.servResp.createResponse(this.response).subscribe((data: Response[]) => {
    //   });
    // }

    this.isAnswer = true;
  }

  back() {
    this.isAnswer = false;
  }

  toLogin() {
    this.router.navigate(['/login']);
  }

  mackeType(fields: Field[]) {
    for (const field of fields) {
      this.type[field.type] = 'radio';
    }
  }

}
