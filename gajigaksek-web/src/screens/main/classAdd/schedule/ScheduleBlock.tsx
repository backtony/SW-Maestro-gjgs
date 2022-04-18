import React from "react";
import "./ScheduleBlock.css";

interface ScheduleBlockProps {
  date: string;
  people: string;
  onClick: () => void;
}

export default class ScheduleBlock extends React.Component<
  ScheduleBlockProps,
  Record<string, never>
> {
  render() {
    return (
      <div className="class-add-schedule-item">
        <div className="class-add-schedule-item-content">
          <div className="class-add-schedule-item-date">
            <i className="far fa-calendar-alt"></i>
            <div className="pretendard">{this.props.date}</div>
          </div>
          <div className="class-add-schedule-item-people">
            <i className="fas fa-user-friends fa"></i>
            <div className="pretendard">{this.props.people}</div>
          </div>
        </div>
        <button onClick={() => this.props.onClick()}>
          <i className="fas fa-times fa"></i>
        </button>
      </div>
    );
  }
}
