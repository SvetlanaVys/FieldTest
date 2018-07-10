import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'formattedTime'
})
export default class FormattedTimePipe implements PipeTransform {
  transform(date: string): string {
    const newDate: Date = new Date(date);
    let dd: any = newDate.getDate();
    if (dd < 10) { dd = '0' + dd; }

    const mm: any = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];

    return dd + ' ' + mm[newDate.getMonth()] + ' ' + newDate.getFullYear() ;

  }
}
