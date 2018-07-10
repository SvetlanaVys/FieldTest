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
  fieldOptionsResult: Object = {};
  options: Option[];
  responses: Response[];
  response: Response;

  type: Object = {
    'Single line text' : 'text',
    'Multiline text'   : 'textarea',
    'Radio button'     : 'radio',
    'Checkbox'         : 'checkbox',
    'Combobox'         : 'option',
    'Date'             : 'date'
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
    this.serv.getAliveFielts().subscribe((data: Field[]) => {
      this.fields = data;
      this.fieldListGen();
    });

  }

  // make array of fields
  fieldListGen() {
    for (const field of this.fields) {
      this.fieldList[field.label] = '';
    }
  }

  // load options
  loadOptions() {
    this.serv.getAllOptions().subscribe((data: Option[]) => {
        this.options = data;
        this.fieldOption();
        this.isReady = true;
    });
  }

  // make array of options for field
  fieldOption() {
    for (const option of this.options) {
      // alert(option.field);
      this.fieldOptions[option.field.id] = [];
      this.fieldOptionsResult[option.field.id] = [];
    }
    for (const option of this.options) {
      this.fieldOptions[option.field.id].push(option);
    }
  }

  // ANSWER PROCESSING
  // receive the option`s answer from user
  saveOptionAnswer(option: Option, isChecked: boolean, type: string) {
    if (type === 'radio') {
      this.fieldOptionsResult[option.field.id][0] = option.name;
    } else {
      if (isChecked) {
        this.fieldOptionsResult[option.field.id].push(option.name);
      } else {
        const index = this.fieldOptionsResult[option.field.id].indexOf(option.name);
        if (index > -1) {
          this.fieldOptionsResult[option.field.id].splice(index, 1);
        }
      }
    }

    this.fieldList[option.field.label] = this.fieldOptionsResult[option.field.id].join(', ');
    // alert(this.fieldOptionsResult[option.field.id].length);
    // for (let i = 0; i < this.fieldOptionsResult[option.field.id].length; i++) {
    //   alert(this.fieldOptionsResult[option.field.id][i]);
    // }

  }

  // save users`s answer
  create() {
    // this.servResp.getResponse().subscribe((data: Response[]) => {
    //     this.responses = data;
    //   for (const response of this.responses) {
    //     alert(response);
    //   }
    // });

    for (const field of this.fields) {
      // alert(field.type);
      if (this.fieldList[field.label] !== '') {
        // alert(this.fieldList[field.label]);
        // if (field.type ===  'Radio button' || field.type === 'Checkbox') {
          // alert(this.fieldOptionsResult[field.id]);
          // this.response = new Response(null, this.fieldOptionsResult[field.id].join(', '), field.rowNumber, field);
        // } else {
        // alert(this.fieldList[field.label]);
          this.response = new Response(null, this.fieldList[field.label], field.rowNumber, field);
        // }
        this.servResp.createResponse(this.response).subscribe(data => {});
      }
      field.rowNumber += 1;
      this.serv.updateField(field.id, field).subscribe(data => {});
    }

    this.isAnswer = true;
  }

  // NAVIGATE
  // back to form after saving
  back() {
    this.isAnswer = false;
    this.loadFields();
    this.loadOptions();
    this.fieldOptionsResult = [];
  }

  // back to the login page
  toLogin() {
    this.router.navigate(['/login']);
  }

  // mackeType(fields: Field[]) {
  //   for (const field of fields) {
  //     this.type[field.type] = 'radio';
  //   }
  // }

}
