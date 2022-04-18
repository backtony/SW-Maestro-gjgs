import React from "react";
import "./SchedulePopupTab.css";

interface SchedulePopupTabProps {
  onClick: () => void;
  title: string;
  on: boolean;
}

export default class ShcedulePopupTab extends React.Component<
  SchedulePopupTabProps,
  Record<string, never>
> {
  render() {
    return (
      <button
        className={`class-add-schedule-popup-tab ${
          this.props.on ? "class-add-schedule-popup-tab-on" : ""
        } pretendard`}
        onClick={() => this.props.onClick()}
      >
        {this.props.title}
      </button>
    );
  }
}
