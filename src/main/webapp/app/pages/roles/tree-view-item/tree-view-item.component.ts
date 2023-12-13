import { Component, Input, OnInit } from '@angular/core';

import { RolesService } from '../roles.service';
import { TreeViewItem } from '../roles';

@Component({
  selector: 'jhi-tree-view-item',
  templateUrl: './tree-view-item.component.html',
  styleUrls: ['./tree-view-item.component.scss'],
})
export class TreeViewItemComponent implements OnInit {
  @Input() tree?: TreeViewItem[];
  @Input() options: any;
  @Input() isChildren: any;
  @Input() isView = false;

  // @Output() onCheckItem = new EventEmitter();
  constructor(private checkService: RolesService) {}

  ngOnInit(): void {}

  updateTree(): void {
    this.checkService.checked();
  }
  onCollapseExpand(node: TreeViewItem): void {
    if (node.children.length) {
      node.collapsed = !node.collapsed;
    }
  }

  onCheck(node: TreeViewItem): void {
    /**
     * đây là một đoạn logic lằng nhằng nhằm mục đích khio tích chọn 1 quyền nào đó
     * trong danh sách viewOnlyRole được khai báo ở trên sẽ tự động bỏ tích chọn
     * ở các checkbox khác cạnh nó (cùng parent), còn khi tích chọn 1 quyền nào đó
     * mà nó không nằm trong danh sách viewOnlyRole thì xem có thằng nào cùng parent
     * mà nằm trong danh sách viewOnlyRole đấy không thì bỏ tích chọn nó
     * @author phuonghv thể theo yêu cầu của chị Hạnh suki iu dấu
     */
    // if (node.checked) {
    //     if (this.viewOnlyRole.includes(node.value)) {
    //         for (let i = 0; i < this.tree.length; i++) {
    //             if (this.tree[i].value !== node.value) {
    //                 this.tree[i].checked = false;
    //                 this.onCheckNode(this.tree[i]);
    //             }
    //         }
    //     } else {
    //         for (let i = 0; i < this.tree.length; i++) {
    //             this.viewOnlyRole.forEach(role => {
    //                 if (this.tree[i].value === role) {
    //                     this.tree[i].checked = false;
    //                     this.onCheckNode(this.tree[i]);
    //                 }
    //             });
    //         }
    //     }
    // }
    if (node.collapsed) {
      this.onCollapseExpand(node);
    }
    this.onCheckNode(node);
  }

  onCheckNode(node: TreeViewItem): void {
    if (node.children.length) {
      node.children.forEach(item => {
        item.checked = node.checked;
        // this.viewOnlyRole.forEach( role => {
        //     if (role.substr(0, role.length - 2) === node.value
        //         && item.value === role) {
        //         item.checked = false;
        //     }
        // });
        this.onCheck(item);
      });
    }
  }
}
