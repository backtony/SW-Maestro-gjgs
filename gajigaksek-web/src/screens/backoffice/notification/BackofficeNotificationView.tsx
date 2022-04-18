import React from "react";
import MemberController from "../../../services/controllers/MemberController";
import { parsedDate } from "../../../utils/commonParams";
import ClassManageButton from "../../main/classManage/ClassManageButton";
import MyclassCouponPopup from "../../main/myClass/MyClassCouponPopup";
import BackofficeNotificationMember from "./BackofficeNotificationMember";
import BackofficeNotificationPopup from "./BackofficeNotificationPopup";
import "./BackofficeNotificationView.css";

interface BackofficeNotificationViewStates {
  startDate: string;
  endDate: string;
  dateFilter: string;
  searchType: string;
  keyword: string;
  members: MemberInfo[];
  checkedMembers: number[];
  popup: boolean;
}

export interface MemberInfo {
  authority: string;
  createdDate: string;
  id: number;
  nickname: string;
  phone: string;
}

export default class BackofficeNotificationView extends React.Component<
  Record<string, never>,
  BackofficeNotificationViewStates
> {
  constructor(props: any) {
    super(props);
    this.state = {
      startDate: "",
      endDate: "",
      dateFilter: "전체",
      searchType: "ALL",
      keyword: "",
      members: [],
      popup: false,
      checkedMembers: [],
    };
  }

  private async getMember() {
    const params = {
      authority: this.state.searchType === "ALL" ? null : this.state.searchType,
      createdDateEnd: this.state.endDate,
      createdDateStart: this.state.startDate,
      nickname: this.state.keyword,
    };
    try {
      const res = await MemberController.getMember(params);
      this.setState({ members: res.data.content, checkedMembers: [] });
    } catch (e) {}
  }

  render() {
    return (
      <div className="myclass-container">
        <div className="myclass-header backoffice-noti-header pretendard">
          알림
        </div>

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
            권한 및 닉네임
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
              <option value="ROLE_DIRECTOR">디렉터</option>
              <option value="ROLE_USER">일반</option>
              <option value="ROLE_ADMIN">관리자</option>
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
                onClick={() => {
                  this.getMember();
                }}
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
              <button
                className={`class-manage-button backoffice-noti-button pretendard class-manage-button-on`}
                onClick={() => this.setState({ popup: true })}
              >
                알림 보내기
              </button>
            </div>
          </div>
        </div>
        <div className="coupon-manage-header">
          <div className="coupon-manage-header1 pretendard">
            <div>
              <i className="far fa-square fa-lg"></i>
            </div>
          </div>
          <div className="coupon-manage-header3 pretendard">
            <div>회원번호</div>
          </div>
          <div className="coupon-manage-header2 pretendard">
            <div>닉네임</div>
          </div>
          <div className="coupon-manage-header4 pretendard">
            <div>전화번호</div>
          </div>
          <div className="coupon-manage-header4 pretendard">
            <div>가입일</div>
          </div>
          <div className="coupon-manage-header5 pretendard">
            <div>권한</div>
          </div>
        </div>
        <div className="coupon-manage-list">
          {this.state.members.map((member: MemberInfo) => (
            <BackofficeNotificationMember
              key={member.id}
              memberId={member.id}
              nickname={member.nickname}
              phone={member.phone}
              startDate={member.createdDate}
              role={member.authority}
              checked={this.state.checkedMembers.includes(member.id)}
              onClick={() => {
                let checkedMembers = this.state.checkedMembers;
                if (checkedMembers.includes(member.id)) {
                  checkedMembers = checkedMembers.filter(
                    (id) => id !== member.id
                  );
                } else {
                  checkedMembers.push(member.id);
                }
                this.setState({ checkedMembers });
              }}
            />
          ))}
        </div>

        <BackofficeNotificationPopup
          memberIds={this.state.checkedMembers}
          on={this.state.popup}
          setVisible={(visible: boolean) => this.setState({ popup: visible })}
        />
      </div>
    );
  }
}
