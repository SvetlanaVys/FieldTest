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
  isOptionsDeleted: Boolean;
  optionString: string;

  constructor(private serv: FieldService) {
    this.editedField = new Field(0, '', '', false, false, 0);
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

  // load options for field
  loadOptions(field: Field) {
    this.serv.getOptionsForField(this.editedField).subscribe((data: Option[]) => {
      this.options = data;
      this.optionString = this.makeOptionsString(this.options);
    });
  }

  // add field
  addField() {
    if (this.fields.length !== 0) {
      this.editedField = new Field(0, '', '', false, false, this.fields[0].rowNumber);
    } else {
      this.editedField = new Field(0, '', '', false, false, 0);
    }
    this.isNewRecord = true;
    this.optionString = '';
  }

  // editing subjects
  editField(field: Field) {
    this.editedField = new Field(field.id, field.label, field.type, field.required, field.isActive, field.rowNumber);
    this.loadOptions(this.editedField);
  }


  // save field
  saveField() {
    // this.saveOptions(this.editedField);
    if (this.isNewRecord) {
      // add field
      this.editedField.id = null;
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
      this.serv.deleteField(field.id).subscribe(data => {
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

  // save options
  saveOptions(field: Field) {
    const optionArr = this.makeOptionsArray(this.optionString);

    for (let i = 0; i < this.options.length; i++) {
      if (optionArr.length > i + 1) {
        this.options[i].name = optionArr[i];
        this.serv.updateOption(this.options[i].id, this.options[i]).subscribe(data => {});
      } else {
        this.options[i].field = null;
        this.serv.deleteOption(this.options[i].id).subscribe(data => {});
      }
    }
    for (let i = this.options.length; i < optionArr.length; i++) {
      if (optionArr[i] !== '') {
        this.serv.createOption(new Option(null, optionArr[i], field)).subscribe(data => {});
      }
    }
  }
}
