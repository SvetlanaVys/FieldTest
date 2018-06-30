import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {Field} from '../shared/models/field';
import {FieldService} from '../shared/services/field.service';
import {Option} from '../shared/models/option';

@Component({
  selector: 'app-field',
  templateUrl: './field.component.html',
  styleUrls: ['./field.component.css'],
  providers: [FieldService]
})
export class FieldComponent implements OnInit {

  fields: Field[];
  options: Option[];
  types: String[];
  editedField: Field;
  isNewRecord: Boolean;
  optionString: string;

  constructor(private serv: FieldService) {
    this.editedField = new Field(0, '', '', false, false, false);
    this.options = [];
    this.types = ['Single line text', 'Multiline text', 'Radio button', 'Checkbox',  'Combobox', 'Date'];
  }


  ngOnInit(): void {
    this.loadFields();
  }

  // load fields
  loadFields() {
    this.serv.getFielts().subscribe((data: Field[]) => {
      this.fields = data;
    });
  }

  // add field
  addField() {
    this.editedField = new Field(0, '', '', false, false, false);
    this.isNewRecord = true;
    this.optionString = '';
  }

  // editing subjects
  editField(field: Field) {
    this.editedField = new Field(field.id, field.label, field.type, field.required, field.isActive, field.isDeleted);

    this.serv.getOptions(this.editedField).subscribe((data: Option[]) => {
      this.options = data;
      this.optionString = this.makeOptionsString(this.options);
    });
  }


  // save field
  saveField() {
    // this.saveOptions(this.editedField);
    if (this.isNewRecord) {
      // add field
      this.serv.createField(this.editedField).subscribe((data: Field) => {
        this.saveOptions(data);
        this.loadFields();
      });
      this.isNewRecord = false;
    } else {
      // edit field
      this.serv.updateField(this.editedField.id, this.editedField).subscribe((data: Field) => {
          this.saveOptions(data);
          this.loadFields();
      });
    }
  }

  deleteField(field: Field) {
    field.isDeleted = true;
    this.serv.updateField(this.editedField.id, this.editedField).subscribe((data: Field) => {
      this.loadFields();
    });
  }
/*---------------------OPTIONS--------------------*/
  makeOptionsString(options: Option[]) {
    let resString: string;
    resString = '';
    for (const option of options) {
      resString += option.name + '\n';
    }
    return resString;
  }

  makeOptionsArray(optionStr: string) {
    let optionArr: string[];
    optionArr = optionStr.split('\n');
    return optionArr;
  }

  saveOptions(field: Field) {
    alert(field);
    const optionArr = this.makeOptionsArray(this.optionString);

    for (let i = 0; i < this.options.length; i++) {
      if (optionArr.length > i + 1) {
        this.options[i].name = optionArr[i];
        alert(this.options[i].name + 'update');
      } else {
        this.options[i].field = null;
        alert(this.options[i].name + 'update set null');
      }
      this.serv.updateOption(this.options[i].id, this.options[i]).subscribe(data => {});
    }
    for (let i = this.options.length; i < optionArr.length; i++) {
      if (optionArr[i] !== '') {
        alert(optionArr[i] + 'create' + field.id);
        this.serv.createOption(new Option(0, optionArr[i], field)).subscribe(data => {});
      }
    }

    // for (let i = 0; i < this.options.length; i++) {
    //   if(this.options[i].field != null){
    //     alert(this.options[i]);
    //
    //   }
    // }

  }
}
