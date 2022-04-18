import React from "react";
import LectureController from "../../../services/controllers/LectureController";
import { parsedDate } from "../../../utils/commonParams";
import ClassManageButton from "./ClassManageButton";
import ClassManageItem from "./ClassManageItem";
import "./ClassManageView.css";

interface ClassManageViewProps {
  path: string;
}

interface ClassManageScheduleInfo {
  currentParticipants: number;
  endHour: number;
  endMinute: number;
  lectureId: number;
  maxParticipants: number;
  priceFour: number;
  priceOne: number;
  priceThree: number;
  priceTwo: number;
  regularPrice: number;
  scheduleDate: string;
  scheduleId: number;
  scheduleStatus: string;
  startHour: number;
  startMinute: number;
  title: string;
}

interface ClassManageViewStates {
  startDate: string;
  endDate: string;
  dateFilter: string;
  searchType: string;
  keyword: string;
  schedules: ClassManageScheduleInfo[];
}

export default class ClassManageView extends React.Component<
  ClassManageViewProps,
  ClassManageViewStates
> {
  constructor(props: any) {
    super(props);
    this.state = {
      startDate: "",
      endDate: "",
      dateFilter: "전체",
      searchType: "ALL",
      keyword: "",
      schedules: [],
    };
  }

  private async getSchedule() {
    const params = {
      endDate: this.state.endDate,
      keyword: this.state.keyword,
      searchType: this.state.searchType,
      startDate: this.state.startDate,
    };

    try {
      const res = await LectureController.getMyClassSchedule(params);
      this.setState({ schedules: res?.data.content });
    } catch (e) {}
  }
  render() {
    return (
      <div className="myclass-container">
        <div className="myclass-header mmm pretendard">클래스 관리</div>

        <div className="class-manage-wrapper">
          <div className="class-add-mainCategory-title pretendard">
            모집 기간 검색
          </div>
          <div className="class-manage-block">
            <div className="class-add-schedule-popup-date-input-wrapper">
              <input
                type="date"
                className="pretendard"
                value={this.state.startDate}
                onChange={(event: any) =>
                  this.setState({
                    startDate: event.target.value,
                    dateFilter: "",
                  })
                }
              ></input>
            </div>
            <div className="dkdk">~</div>
            <div className="class-add-schedule-popup-date-input-wrapper">
              <input
                type="date"
                className="pretendard"
                min={this.state.startDate}
                value={this.state.endDate}
                onChange={(event: any) =>
                  this.setState({ endDate: event.target.value, dateFilter: "" })
                }
              ></input>
            </div>

            <div className="class-manage-button-wrapper">
              <ClassManageButton
                title="전체"
                on={this.state.dateFilter === "전체"}
                onClick={() =>
                  this.setState({
                    startDate: "",
                    endDate: "",
                    dateFilter: "전체",
                  })
                }
              />
              <ClassManageButton
                title="1주일"
                on={this.state.dateFilter === "1주일"}
                onClick={() => {
                  const endDate = new Date();
                  const startDate = new Date();
                  startDate.setDate(endDate.getDate() - 7);
                  this.setState({
                    startDate: parsedDate(startDate),
                    endDate: parsedDate(endDate),
                    dateFilter: "1주일",
                  });
                }}
              />
              <ClassManageButton
                title="1개월"
                on={this.state.dateFilter === "1개월"}
                onClick={() => {
                  const endDate = new Date();
                  const startDate = new Date();
                  startDate.setDate(endDate.getDate() - 31);
                  this.setState({
                    startDate: parsedDate(startDate),
                    endDate: parsedDate(endDate),
                    dateFilter: "1개월",
                  });
                }}
              />
              <ClassManageButton
                title="1년"
                on={this.state.dateFilter === "1년"}
                onClick={() => {
                  const endDate = new Date();
                  const startDate = new Date();
                  startDate.setDate(endDate.getDate() - 365);
                  this.setState({
                    startDate: parsedDate(startDate),
                    endDate: parsedDate(endDate),
                    dateFilter: "1년",
                  });
                }}
              />
            </div>
          </div>
        </div>

        <div className="class-manage-wrapper">
          <div className="class-add-mainCategory-title pretendard">
            클래스 상태 키워드
          </div>
          <div className="class-manage-block">
            <select
              className="class-add-curriculum-info-select pretendard meme"
              value={this.state.searchType}
              onChange={(e: any) =>
                this.setState({ searchType: e.target.value })
              }
            >
              <option value="ALL">전체</option>
              <option value="RECRUIT">모집 중</option>
              <option value="HOLD">모집 보류</option>
              <option value="CANCEL">모집 취소</option>
              <option value="FULL">인원 마감</option>
              <option value="CLOSE">확정 및 마감</option>
              <option value="END">완료</option>
              {/* ALL, RECRUIT, HOLD, CANCEL, FULL, CLOSE, END */}
            </select>
            <div className="class-manage-input-wrapper">
              <i className="fas fa-search fa"></i>
              <input
                className="pretendard"
                value={this.state.keyword}
                onChange={(e: any) =>
                  this.setState({ keyword: e.target.value })
                }
              ></input>
            </div>
            <div className="class-manage-button-wrapper">
              <ClassManageButton
                title="검색"
                on
                onClick={() => this.getSchedule()}
              />
              <ClassManageButton
                title="초기화"
                on={false}
                onClick={() =>
                  this.setState({
                    endDate: "",
                    startDate: "",
                    keyword: "",
                    dateFilter: "전체",
                    searchType: "ALL",
                  })
                }
              />
            </div>
          </div>
        </div>

        <div className="class-manage-list-header">
          <div className="pretendard">
            <div>#</div>
          </div>
          <div className="pretendard">
            <div>클래스명</div>
          </div>
          <div className="pretendard">
            <div>신청인원</div>
          </div>
          <div className="pretendard">
            <div>스케줄</div>
          </div>
          <div className="pretendard">
            <div>가격</div>
          </div>
          <div className="pretendard">
            <div>상태</div>
          </div>
          <div className="pretendard">
            <div>검수상태</div>
          </div>
        </div>
        <div className="class-manage-list">
          {this.state.schedules.map(
            (schedule: ClassManageScheduleInfo, index: number) => (
              <ClassManageItem
                idx={index}
                title={schedule.title}
                people={`${schedule.currentParticipants}/${schedule.maxParticipants}`}
                date={`${schedule.scheduleDate} ${schedule.startHour}:${schedule.startMinute} ~ ${schedule.endHour}:${schedule.endMinute}`}
                price={`${schedule.regularPrice}원`}
                status1={schedule.scheduleStatus}
                status2={"검수완료"}
              />
            )
          )}
        </div>
      </div>
    );
  }
}
