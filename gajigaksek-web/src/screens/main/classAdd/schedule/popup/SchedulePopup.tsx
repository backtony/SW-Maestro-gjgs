import React from "react";
import SchedulePopupTab from "./SchedulePopupTab";
import "./SchedulePopup.css";
import SchedulePopupDayButton from "./SchedulePopupDayButton";
import { enDayList, koreanDayType } from "../../../../../utils/commonParams";

export class ScheduleInfo {
  constructor() {
    this.lectureProgressMinute = this.startHour = this.startMinute = 0;
    this.lectureDate = "20210901";
  }
  lectureProgressMinute: number;
  lectureDate: string;
  startHour: number;
  startMinute: number;
}

interface SchedulePopupProps {
  on: boolean;
  progressMinute: number;
  onClickExit: () => void;
  addSchedule: (infoList: ScheduleInfo[]) => void;
}

interface SchedulePopupStates {
  tab: number;
  info: ScheduleInfo | undefined;
  dayTypeList: string[];
  startDate: string;
  endDate: string;
  startTime: string;
  endTime: string;
  minPeople: number;
  maxPeople: number;
}

export default class SchedulePopup extends React.Component<
  SchedulePopupProps,
  SchedulePopupStates
> {
  constructor(props: any) {
    super(props);
    this.state = {
      tab: 0,
      info: undefined,
      dayTypeList: [],
      startDate: "",
      endDate: "",
      startTime: "",
      endTime: "",
      minPeople: 1,
      maxPeople: 1,
    };
  }

  private addDayType(dayType: string) {
    let updatedList = [...this.state.dayTypeList, dayType];

    if (this.state.dayTypeList.includes(dayType)) {
      updatedList = updatedList.filter((value) => value !== dayType);
      this.setState({ dayTypeList: updatedList });
      return;
    }

    this.setState({ dayTypeList: updatedList });
  }

  private makeFormattedDate(date: Date) {
    const yyyy = date.getFullYear();
    const mm = date.getMonth() + 1;
    const dd = date.getDate();

    return `${yyyy}-${mm < 10 ? "0" : ""}${mm}-${dd < 10 ? "0" : ""}${dd}`;
  }

  private handleSubmit() {
    if (!this.checkForm()) return;
    const itemList: ScheduleInfo[] = [];
    if (this.state.tab === 0) {
      const date = new Date(this.state.startDate);
      const limitDate = new Date(this.state.endDate);
      limitDate.setDate(limitDate.getDate() + 1);
      const formattedLimitDate = this.makeFormattedDate(limitDate);
      let formattedDate = this.makeFormattedDate(date);

      while (formattedDate !== formattedLimitDate) {
        if (this.state.dayTypeList.includes(enDayList[date.getDay()]))
          itemList.push(this.makeScheduleInfo(formattedDate));
        date.setDate(date.getDate() + 1);
        formattedDate = this.makeFormattedDate(date);
      }
    } else if (this.state.tab === 1) {
      itemList.push(this.makeScheduleInfo(this.state.startDate));
    }

    this.props.addSchedule(itemList);
    this.props.onClickExit();
  }

  private makeScheduleInfo(startDate: string) {
    const scheduleObj: ScheduleInfo = new ScheduleInfo();
    scheduleObj.lectureDate = startDate;
    scheduleObj.lectureProgressMinute = this.props.progressMinute;
    scheduleObj.startHour = +this.state.startTime.split(":")[0];
    scheduleObj.startMinute = +this.state.startTime.split(":")[1];
    return scheduleObj;
  }

  private setEndTime(startTime: string) {
    if (!startTime) return;

    let hh: number = +startTime.split(":")[0];

    let mm: number = +startTime.split(":")[1];

    mm = +mm + +(hh * 60) + this.props.progressMinute;
    hh = +Math.floor(mm / 60);
    mm = mm % 60;

    hh %= 24;

    this.setState({
      endTime: `${hh < 10 ? "0" : ""}${hh}:${mm < 10 ? "0" : ""}${mm}`,
    });
  }

  private checkForm() {
    if (!this.state.startDate) {
      alert("시작날짜를 입력해주세요.");
      return false;
    }

    if (!this.state.startTime) {
      alert("시작시간을 입력해주세요.");
      return false;
    }

    const hh: number = +this.state.startTime.split(":")[0];

    const mm: number = +this.state.startTime.split(":")[1];

    const totalMinute = +mm + +(hh * 60);

    if (totalMinute % 30 > 0) {
      alert("시작시간은 30분 단위로 입력해주세요. (ex) 16시 30분, 18시 00분");
      return false;
    }

    if (this.state.tab === 0 && !this.state.endDate) {
      alert("종료날짜를 입력해주세요.");
      return false;
    }

    if (this.state.tab === 0 && this.state.dayTypeList.length <= 0) {
      alert("요일을 선택해주세요.");
      return false;
    }

    return true;
  }

  render() {
    return (
      <div
        className={`home-popup-container ${
          this.props.on ? "home-popup-container-on" : ""
        }`}
      >
        <div className="class-add-schedule-popup">
          <div className="home-popup-title-container">
            <div className="home-popup-title pretendard">스케줄 추가</div>
            <button
              className="home-popup-exit"
              onClick={() => this.props.onClickExit()}
            >
              <i className="fas fa-times fa-2x"></i>
            </button>
          </div>
          <div className="class-add-schedule-popup-main-container">
            <div className="class-add-schedule-popup-tab-wrapper">
              <SchedulePopupTab
                title="요일 반복"
                on={this.state.tab === 0}
                onClick={() => this.setState({ tab: 0 })}
              />
              <SchedulePopupTab
                title="개별 선택"
                on={this.state.tab === 1}
                onClick={() => this.setState({ tab: 1 })}
              />
            </div>
            <div className="class-add-schedule-popup-date-wrapper">
              <div className="class-add-schedule-popup-date-title pretendard">
                강의 기간
              </div>
              <div className="class-add-schedule-popup-date-input-container">
                <div className="class-add-schedule-popup-date-input-wrapper">
                  <input
                    type="date"
                    className="pretendard"
                    value={this.state.startDate}
                    onChange={(event: any) =>
                      this.setState({ startDate: event.target.value })
                    }
                  ></input>
                </div>
                {this.state.tab === 0 && <div>~</div>}
                {this.state.tab === 0 && (
                  <div className="class-add-schedule-popup-date-input-wrapper">
                    <input
                      type="date"
                      className="pretendard"
                      min={this.state.startDate}
                      value={this.state.endDate}
                      onChange={(event: any) =>
                        this.setState({ endDate: event.target.value })
                      }
                    ></input>
                  </div>
                )}
              </div>
            </div>

            {this.state.tab === 0 && (
              <div className="class-add-schedule-popup-day-wrapper">
                <div className="class-add-schedule-popup-date-title pretendard">
                  강의 요일
                </div>
                <div className="class-add-schedule-popup-day-list">
                  {Object.keys(koreanDayType).map((key) => (
                    <SchedulePopupDayButton
                      title={key}
                      onClick={() => this.addDayType(koreanDayType[key])}
                      on={this.state.dayTypeList.includes(koreanDayType[key])}
                    />
                  ))}
                </div>
              </div>
            )}

            <div className="class-add-schedule-popup-day-wrapper">
              <div className="class-add-schedule-popup-date-title pretendard">
                시작 시간
              </div>
              <div className="class-add-schedule-popup-date-input-container add-flex-start">
                <div className="class-add-schedule-popup-date-input-wrapper">
                  <input
                    type="time"
                    step="1800"
                    className="pretendard"
                    value={this.state.startTime}
                    onChange={(event: any) =>
                      this.setState({ startTime: event.target.value }, () =>
                        this.setEndTime(this.state.startTime)
                      )
                    }
                  ></input>
                </div>
                <div className="margin-vertical">~</div>
                <div className="class-add-schedule-popup-date-input-wrapper">
                  <input
                    type="time"
                    step="1800"
                    className="pretendard"
                    value={this.state.endTime}
                    min={this.state.startTime}
                  ></input>
                </div>
              </div>
            </div>
            <button className="pretendard" onClick={() => this.handleSubmit()}>
              추가
            </button>
          </div>
        </div>
      </div>
    );
  }
}
