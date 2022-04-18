import React from "react";
import "./CurriculumItemView.css";
import { CurriculumItem } from "./CurriculumView";

interface CurriculumItemProps {
  onClickUp: () => void;
  onClickDown: () => void;
  item: CurriculumItem;
}

export default class CurriculumItemView extends React.Component<
  CurriculumItemProps,
  Record<string, never>
> {
  render() {
    return (
      <div className="class-add-curriculum-table-item">
        <div className="class-add-curriculum-order">
          <button onClick={() => this.props.onClickUp()}>
            <i className="fas fa-arrow-up fa-lg"></i>
          </button>
          <button onClick={() => this.props.onClickDown()}>
            <i className="fas fa-arrow-down fa-lg"></i>
          </button>
        </div>
        <div className="class-add-curriculum-table-item-img-wrapper">
          <img title={this.props.item.imgTitle} src={this.props.item.imgSrc} />
        </div>
        <div className="class-add-curriculum-table-item-title-wrapper">
          <div>{this.props.item.title}</div>
        </div>
        <div className="class-add-curriculum-table-item-detail-wrapper">
          <div className="pretendard">{this.props.item.text}</div>
        </div>
      </div>
    );
  }
}
