import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { RolesService } from '../roles.service';
import { TreeViewItem } from '../roles';

@Component({
  selector: 'jhi-tree-view',
  templateUrl: './tree-view.component.html',
  styleUrls: ['./tree-view.component.scss'],
})
export class TreeViewComponent implements OnInit {
  @Input() tree?: TreeViewItem[];
  selectedItem?: TreeViewItem[];
  @Input() listSelected?: any = [];
  @Output() sendListEvent = new EventEmitter<number[]>();
  @Input() isView = false;

  constructor(private checkService: RolesService) {
    this.checkService.checkEvent.subscribe(() => {
      this.selectedItem = [];
      this.tree!.forEach(item => {
        this.updateTree(item);
      });
    });
  }

  /**
   * Lại là mấy vòng for vớ vẩn nữa để check nếu 1 children mà parent nó là ae với 1 role trong list
   * thì bỏ check thằng role đó, ai đó nếu có ý tưởng hay hơn thì sửa giùm vs, đoạn này chỉ check ngược được 1 tầng thui
   * @Author phuonghv
   * @param node
   */
  updateTree(node: TreeViewItem): void {
    if (node.children.length) {
      node.checked = false;
      node.children.forEach(item => {
        this.updateTree(item);
        if (item.checked) {
          node.checked = true;
        }
        // if (this.viewOnlyRole.includes(item.value)) {
        //     node.children.forEach(crole => {
        //        if (crole.checked && !this.viewOnlyRole.includes(crole.value)) {
        //            node.children.forEach(drole => {
        //               if (this.viewOnlyRole.includes(drole.value)) {
        //                   drole.checked = false;
        //               }
        //            });
        //        }
        //     });
        // }
      });
    }
    if (node.checked) {
      this.selectedItem!.push(node);
    }
  }

  ngOnInit(): void {}

  onToggle(isExpand: any): void {
    this.tree!.forEach(item => {
      this.setCollapsedAll(item, isExpand);
    });
  }

  setCollapsedAll(node: any, isExpand: any): void {
    node.collapsed = isExpand;
    node.children.forEach((item: any) => {
      this.setCollapsedAll(item, isExpand);
    });
  }

  sendListSelected() {
    this.listSelected = [];
    // @ts-ignore
    if (this.selectedItem?.length > 0) {
      // @ts-ignore
      for (const item of this.selectedItem) {
        if (item.checked == true) {
          this.listSelected.push(item.id);
        }
      }
    }
    const listToSend: number[] = this.listSelected;
    this.sendListEvent.emit(listToSend);
  }

  ngAfterViewInit(): void {
    setTimeout(() => this.checkTree(), 500);
  }

  checkTree() {
    // @ts-ignore
    for (const item of this.tree) {
      if (this.listSelected.includes(item.id)) {
        item.checked = true;
        this.selectedItem?.push(item);
      }
      if (item.children.length > 0) {
        for (const child of item.children) {
          if (this.listSelected.includes(child.id)) {
            child.checked = true;
            this.selectedItem?.push(child);
          }
          if (child.children.length > 0) {
            for (const child1 of child.children) {
              if (this.listSelected.includes(child1.id)) {
                child1.checked = true;
                this.selectedItem?.push(child1);
              }
            }
          }
        }
      }
    }
  }
}
