import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'formattedNull'
})
export default class FormattedNullPipe implements PipeTransform {
  transform(data: string): string {
    if (data === undefined ) {
      return 'N/A';
    } else {
      return data;
    }
  }
}
