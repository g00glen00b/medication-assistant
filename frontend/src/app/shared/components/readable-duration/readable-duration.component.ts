import {Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {formatDuration} from 'date-fns';
import {parseAndNormalizeDuration} from "../../utilities/duration-utilities";

@Component({
  selector: 'mediminder-readable-duration',
  templateUrl: './readable-duration.component.html',
  styleUrls: ['./readable-duration.component.scss']
})
export class ReadableDurationComponent implements OnChanges {
  @Input()
  duration!: string;
  durationString: string = '';

  ngOnChanges(changes: SimpleChanges): void {
    const durationObject = parseAndNormalizeDuration(this.duration);
    this.durationString = formatDuration(durationObject);
  }
}
