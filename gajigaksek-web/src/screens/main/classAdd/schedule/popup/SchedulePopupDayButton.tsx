import React from "react";
import "./SchedulePopupDayButton.css";

interface SchedulePopupDayButtonProps {
  title: string;
  onClick: () => void;
  on: boolean;
}

export default class SchedulePopupDayButton extends React.Component<
  SchedulePopupDayButtonProps,
  Record<string, never>
> {
  render() {
    return (
      <button
        className={`class-add-schedule-day-button pretendard ${
          this.props.title === "일" ? "class-add-schedule-sun" : ""
        } ${this.props.title === "토" ? "class-add-schedule-sat" : ""} ${
          this.props.on ? "class-add-schedule-day-button-on" : ""
        }`}
        onClick={() => this.props.onClick()}
      >
        {this.props.title}
      </button>
    );
  }
}
