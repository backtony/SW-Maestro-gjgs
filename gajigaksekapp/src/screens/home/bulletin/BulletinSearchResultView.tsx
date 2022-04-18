import React from 'react';
import {StyleSheet, FlatList} from 'react-native';
import {View} from 'react-native';
import BulletinApiController from '../../../services/apis/BulletinApiController';
import Zone from '../../../utils/Zone';
import BulletinSearchResultViewHeader from './components/bulletinSearchResultView/BulletinSearchResultViewHeader';
import BulletinSearchResultViewCount from './components/bulletinSearchResultView/BulletinSearchResultViewCount';
import BulletinSearchResultViewZone from './components/bulletinSearchResultView/BulletinSearchResultViewZone';
import BulletinSearchResultViewBulletin from './components/bulletinSearchResultView/BulletinSearchResultViewBulletin';

export default class BulletinSearchResultView extends React.Component<
  {},
  {
    keyword: string;
    data: any;
    zoneModalVisible: boolean;
    selectedSubZone: string;
    page: number;
    refreshing: boolean;
    totalElements: number;
  }
> {
  constructor(props: any) {
    super(props);
    this.state = {
      keyword: '',
      data: [],
      zoneModalVisible: false,
      selectedSubZone: '',
      page: 0,
      refreshing: false,
      totalElements: 0,
    };
  }
  componentDidMount() {
    this.setState({keyword: this.props.route.params.keyword}, () => {
      this.getBulletinListApi();
    });
  }

  private async getBulletinListApi() {
    try {
      const res = await BulletinApiController.getSearchBulletin({
        page: this.state.page ? this.state.page : 0,
        size: 10,
        keyword: this.state.keyword,
        zoneId: this.state.selectedSubZone
          ? Zone.getId(this.state.selectedSubZone)
          : this.state.selectedSubZone,
      });

      this.setState({
        data:
          this.state.page === 0
            ? res.data.content
            : [...this.state.data, ...res.data.content],
        refreshing: false,
        totalElements: res.data.totalElements,
      });
    } catch (e) {
      console.error(e);
    }
  }

  private LoadMoreBulletin() {
    this.setState({page: this.state.page + 1}, () => {
      this.getBulletinListApi();
    });
  }

  render() {
    return (
      <View style={{flex: 1, backgroundColor: '#FFF'}}>
        <BulletinSearchResultViewHeader
          navigation={this.props.navigation}
          keyword={this.state.keyword ? this.state.keyword : ''}
          setKeyword={(text: string) => this.setState({keyword: text})}
          onSubmit={() =>
            this.setState({page: 0}, () => {
              this.getBulletinListApi();
            })
          }
        />
        <View style={{backgroundColor: '#FFF', flex: 1}}>
          <BulletinSearchResultViewCount
            totalElements={this.state.totalElements}
          />
          <BulletinSearchResultViewZone
            subZone={this.state.selectedSubZone}
            setParentSubZone={(sub: string) =>
              this.setState({selectedSubZone: sub, page: 0}, () => {
                this.getBulletinListApi();
              })
            }
          />

          <FlatList
            style={this.styles.list}
            contentContainerStyle={this.styles.listContainer}
            showsVerticalScrollIndicator={false}
            showsHorizontalScrollIndicator={false}
            columnWrapperStyle={{justifyContent: 'space-between'}}
            data={this.state.data}
            horizontal={false}
            numColumns={2}
            keyExtractor={item => {
              return item.bulletinId;
            }}
            ItemSeparatorComponent={() => {
              return <View style={this.styles.separator} />;
            }}
            renderItem={post => {
              const item = post.item;
              return (
                <BulletinSearchResultViewBulletin
                  navigation={this.props.navigation}
                  bulletinInfo={item}
                />
              );
            }}
            refreshing={this.state.refreshing}
            onRefresh={() =>
              this.setState({page: 0}, () => {
                this.getBulletinListApi();
              })
            }
            onEndReachedThreshold={0}
            onEndReached={() => {
              this.LoadMoreBulletin();
            }}
          />
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    list: {
      paddingHorizontal: 10,
    },
    listContainer: {
      marginHorizontal: 20,
      marginTop: 10,
    },
    separator: {
      marginTop: 10,
    },
  });
}
