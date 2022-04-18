import React from "react";
import AdminLectureController from "../../../../../services/controllers/AdminLectureController";
import ClassAddNaviButton from "../../../../main/classAdd/ClassAddNaviButton";
import CurriculumItemView from "../../../../main/classAdd/curriculum/CurriculumItemView";
import { CurriculumItem } from "../../../../main/classAdd/curriculum/CurriculumView";
import Cookies from "universal-cookie";

interface BackofficeCurriculumViewStates {
  text: string;
  imgOn: boolean;
  curriculumTitle: string;
  curriculumItems: CurriculumItem[];
}

const cookies = new Cookies();

export default class BackofficeCurriculumView extends React.Component<
  Record<string, never>,
  BackofficeCurriculumViewStates
> {
  constructor(props: any) {
    super(props);
    this.state = {
      text: "",
      imgOn: false,
      curriculumTitle: "",
      curriculumItems: [],
    };
    this.getDetail(+cookies.get("admin-lectureId"));
  }

  private async getDetail(lectureId: number) {
    try {
      const res = await AdminLectureController.getDetail(lectureId);
      const data = res?.data;
      this.setState({
        curriculumItems: data.curriculumList.map((info: any) => {
          return {
            imgSrc: info.curriculumImageUrl,
            imgTitle: info.curriculumImageName,
            title: info.title,
            text: info.detailText,
          };
        }),
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
          <ClassAddNaviButton on title={"커리큘럼"} />
          <ClassAddNaviButton on={false} title={"스케줄"} />
          <ClassAddNaviButton on={false} title={"가격 및 쿠폰"} />
          <ClassAddNaviButton on={false} title={"부가정보"} />
        </div>
        <div className="class-add-curriculum-table-wrapper">
          <div className="class-add-curriculum-table-header">
            <div className="sos11 class-add-curriculum-table-header1 pretendard">
              순서
            </div>
            <div className="sos11 class-add-curriculum-table-header2 pretendard">
              사진
            </div>
            <div className="sos11 class-add-curriculum-table-header3 pretendard">
              제목
            </div>
            <div className="sos11 class-add-curriculum-table-header4 pretendard">
              내용
            </div>
          </div>
          {this.state.curriculumItems.map((item: CurriculumItem) => (
            <CurriculumItemView
              onClickUp={() => {
                let dd;
                // const items = this.state.curriculumItems;
                // const idx = items.indexOf(item);
                // if (idx <= 0) return;

                // const temp = items[idx];
                // items[idx] = items[idx - 1];
                // items[idx - 1] = temp;

                // this.setState({ curriculumItems: items });
              }}
              onClickDown={() => {
                let dd;
                // const items = this.state.curriculumItems;
                // const idx = items.indexOf(item);
                // if (idx >= items.length - 1) return;

                // const temp = items[idx];
                // items[idx] = items[idx + 1];
                // items[idx + 1] = temp;

                // this.setState({ curriculumItems: items });
              }}
              item={item}
            />
          ))}
        </div>
        <div className="class-add-first-bottom">
          <div>
            <button
              className="class-add-intro-bottom-left pretendard"
              onClick={() =>
                (window.location.href = "/backoffice/class-detail/intro")
              }
            >
              이전
            </button>
            <button
              className="class-add-first-bottom-left pretendard"
              onClick={() =>
                (window.location.href = "/backoffice/class-detail/schedule")
              }
            >
              다음
            </button>
          </div>
        </div>
      </div>
    );
  }
}
