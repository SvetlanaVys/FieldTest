import {Validator, AbstractControl, NG_VALIDATORS, Validators} from '@angular/forms';
import { Directive, Input } from '@angular/core';

@Directive({
  selector: '[appConfirmEqualValidator]',
  providers: [{
    provide: NG_VALIDATORS,
    useExisting: ConfirmEqualValidatorDirective,
    multi: true
  }]
})
export class ConfirmEqualValidatorDirective implements Validator {
  @Input('appConfirmEqualValidator') appConfirmEqualValidator: AbstractControl;
  validate(control: AbstractControl): { [key: string]: any } | null {
    if (this.appConfirmEqualValidator.value !== control.value) {
      return { 'notEqual': true };
    }

    return null;
  }
}

