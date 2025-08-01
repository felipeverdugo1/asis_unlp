import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule],
  selector: 'app-filename-input-dialog',
  templateUrl: './filename-input-dialog.component.html',
  styleUrls: ['../../../styles.css']
})
export class FilenameInputDialogComponent {
  @Output() saved = new EventEmitter<string>();
  @Output() closed = new EventEmitter<void>();

  filterName = '';
  maxLength = 15;

  validateInput() {
    if (this.filterName.length > this.maxLength) {
      this.filterName = this.filterName.substring(0, this.maxLength);
    }
  }

  save() {
    if (this.isValidName()) {
      this.saved.emit(this.filterName);
    }
  }

  close() {
    this.closed.emit();
  }

  isValidName(): boolean {
    return this.filterName.length > 0 && 
           this.filterName.length <= this.maxLength;
  }
}