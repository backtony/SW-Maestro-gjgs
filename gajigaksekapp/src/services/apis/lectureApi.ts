import {LectureParam} from '../../components/LectureList';

const lectureApi = (params: LectureParam) => {
  console.log('lectureApi: ', params);
  return {
    lectureList: [
      {
        lectureId: 1,

        image:
          'https://images.unsplash.com/flagged/photo-1561668038-2742fcef75d7?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=750&q=80',

        zoneId: 2,

        title: '마카롱 좋아하는 사람들 모여라',

        priceList: {
          one: 50000,
          two: 49000,
          three: 47500,
          four: 46500,
        },
        favorite: true,
      },
      {
        lectureId: 2,

        image:
          'https://images.unsplash.com/photo-1532499016263-f2c3e89de9cd?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=80',

        zoneId: 2,

        title: '16년차 베이커리 장인의 케이크 클래스',

        priceList: {
          one: 50000,
          two: 49000,
          three: 47500,
          four: 46500,
        },
        favorite: false,
      },
      {
        lectureId: 3,

        image:
          'https://images.unsplash.com/photo-1551632811-561732d1e306?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=750&q=80',

        zoneId: 2,

        title: '겨울철 함꼐하는 하이킹 모임',

        priceList: {
          one: 50000,
          two: 49000,
          three: 47500,
          four: 46500,
        },
        favorite: true,
      },
      {
        lectureId: 4,

        image:
          'https://images.unsplash.com/photo-1603455778956-d71832eafa4e?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=751&q=80',

        zoneId: 2,

        title: '10km 러닝 클래스',

        priceList: {
          one: 50000,
          two: 49000,
          three: 47500,
          four: 46500,
        },
        favorite: true,
      },
      {
        lectureId: 5,

        image: 'https://via.placeholder.com/400x200/FFB6C1/000000',

        zoneId: 2,

        title: '마카롱 만들기',

        priceList: {
          one: 50000,
          two: 49000,
          three: 47500,
          four: 46500,
        },
        favorite: true,
      },
      {
        lectureId: 6,

        image: 'https://via.placeholder.com/400x200/FFB6C1/000000',

        zoneId: 2,

        title: '마카롱 만들기',

        priceList: {
          one: 50000,
          two: 49000,
          three: 47500,
          four: 46500,
        },
        favorite: true,
      },
      {
        lectureId: 7,

        image: 'https://via.placeholder.com/400x200/FFB6C1/000000',

        zoneId: 2,

        title: '마카롱 만들기',

        priceList: {
          one: 50000,
          two: 49000,
          three: 47500,
          four: 46500,
        },
        favorite: true,
      },
      {
        lectureId: 8,

        image: 'https://via.placeholder.com/400x200/FFB6C1/000000',

        zoneId: 2,

        title: '마카롱 만들기',

        priceList: {
          one: 50000,
          two: 49000,
          three: 47500,
          four: 46500,
        },
        favorite: true,
      },
      {
        lectureId: 9,

        image: 'https://via.placeholder.com/400x200/FFB6C1/000000',

        zoneId: 2,

        title: '마카롱 만들기',

        priceList: {
          one: 50000,
          two: 49000,
          three: 47500,
          four: 46500,
        },
        favorite: true,
      },
      {
        lectureId: 10,

        image: 'https://via.placeholder.com/400x200/FFB6C1/000000',

        zoneId: 2,

        title: '마카롱 만들기',

        priceList: {
          one: 50000,
          two: 49000,
          three: 47500,
          four: 46500,
        },
        favorite: true,
      },
      {
        lectureId: 11,

        image: 'https://via.placeholder.com/400x200/FFB6C1/000000',

        zoneId: 2,

        title: '마카롱 만들기',

        priceList: {
          one: 50000,
          two: 49000,
          three: 47500,
          four: 46500,
        },
        favorite: true,
      },
      {
        lectureId: 12,

        image: 'https://via.placeholder.com/400x200/FFB6C1/000000',

        zoneId: 2,

        title: '마카롱 만들기',

        priceList: {
          one: 50000,
          two: 49000,
          three: 47500,
          four: 46500,
        },
        favorite: true,
      },
    ],
  };
};

export default lectureApi;
