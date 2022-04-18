import React from "react";
import AdminLectureController from "../../../../../services/controllers/AdminLectureController";
import Cookies from "universal-cookie";
import { ScheduleInfo } from "../../../../main/classAdd/schedule/popup/SchedulePopup";
import ScheduleBlock from "../../../../main/classAdd/schedule/ScheduleBlock";
import ClassAddNaviButton from "../../../../main/classAdd/ClassAddNaviButton";
import { koDayList } from "../../../../../utils/commonParams";

interface BackofficeScheduleViewStates {
  schedules: ScheduleInfo[];
  popup: boolean;
  progressHour: number;
  progressMinute: number;
  minPeople: number;
  maxPeople: number;
}

const hour = [
  0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21,
  22, 23, 24,
];

const minute = [0, 30];

const cookies = new Cookies();

export default class BackofficeScheduleView extends React.Component<
  Record<string, never>,
  BackofficeScheduleViewStates
> {
  constructor(props: any) {
    super(props);
    this.state = {
      schedules: [],
      popup: false,
      progressHour: 2,
      progressMinute: 0,
      minPeople: 1,
      maxPeople: 1,
    };
    this.getDetail(cookies.get("admin-lectureId"));
  }

  private async getDetail(lectureId: number) {
    try {
      const res = await AdminLectureController.getDetail(lectureId);
      if (!res) return;

      this.setState({
        schedules: res.data.scheduleList,
        maxPeople: res.data.maxParticipants,
        minPeople: res.data.minParticipants,
      });
    } catch (e) {}
  }

  render() {
    return (
      <div className="class-add-first-container">
        <div className="class-add-first-header">
          <button
            className="class-add-first-back"
            onClick={() => (window.location.href = "/backoffice/class")}
          >
            <i className="fas fa-chevron-left fa-3x"></i>
          </button>
          <div className="class-add-first-header-title pretendard">
            클래스 검수
          </div>
        </div>
        <div className="class-add-first-navigation">
          <ClassAddNaviButton on={false} title={"기본정보"} />
          <ClassAddNaviButton on={false} title={"상세 소개"} />
          <ClassAddNaviButton on={false} title={"커리큘럼"} />
          <ClassAddNaviButton on title={"스케줄"} />
          <ClassAddNaviButton on={false} title={"가격 및 쿠폰"} />
          <ClassAddNaviButton on={false} title={"부가정보"} />
        </div>
        <div className="class-add-first-main-container">
          <div className="class-add-first-name-container">
            <div className="class-add-schedule-item-container">
              {this.state.schedules.map((item: ScheduleInfo) => {
                const date = new Date(item.lectureDate);

                const startDateFormat = `${date.getFullYear()}.${
                  date.getMonth() + 1
                }.${date.getDate()}(${koDayList[date.getDay()]}) ${
                  item.startHour < 10 ? "0" : ""
                }${item.startHour}시 ${item.startMinute < 10 ? "0" : ""}${
                  item.startMinute
                }분`;

                const endTimeMinute =
                  +(+item.startHour * 60) +
                  +item.startMinute +
                  +(+this.state.progressHour * 60) +
                  +this.state.progressMinute;

                const endHour = Math.floor(endTimeMinute / 60);

                const endMinute = endTimeMinute % 60;

                const endDateFormat = `${date.getFullYear()}.${
                  date.getMonth() + 1
                }.${date.getDate()}(${koDayList[date.getDay()]}) ${
                  endHour < 10 ? "0" : ""
                }${endHour}시 ${endMinute < 10 ? "0" : ""}${endMinute}분`;

                return (
                  <ScheduleBlock
                    date={`${startDateFormat} ~ ${endDateFormat}`}
                    people={`${this.state.minPeople} ~ ${this.state.maxPeople}명`}
                    onClick={() => {
                      let dd;
                      // this.setState({
                      //   schedules: this.state.schedules.filter(
                      //     (schedule) => schedule !== item
                      //   ),
                      // });
                    }}
                  />
                );
              })}
            </div>
          </div>
        </div>
        <div className="class-add-first-bottom">
          <div>
            <button
              className="class-add-intro-bottom-left pretendard"
              onClick={() =>
                (window.location.href = "/backoffice/class-detail/curriculum")
              }
            >
              이전
            </button>
            <button
              className="class-add-first-bottom-left pretendard"
              onClick={() => {
                window.location.href = "/backoffice/class-detail/price-coupon";
              }}
            >
              다음
            </button>
          </div>
        </div>
      </div>
    );
  }
}
