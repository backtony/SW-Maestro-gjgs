import React from 'react';
import {StyleSheet, View, ScrollView, FlatList} from 'react-native';
import Category from '../../../utils/Category';
import BulletinApiController from '../../../services/apis/BulletinApiController';
import FavoriteApiController from '../../../services/apis/FavoriteApiController';
import BulletinMainViewHeader from './components/bulletinMainView/BulletinMainViewHeader';
import BulletinMainViewMatchingButton from './components/bulletinMainView/BulletinMainViewMatchingButton';
import BulletinMainViewCategoryButton from './components/bulletinMainView/BulletinMainViewCategoryButton';
import BulletinMainViewBulletinBlock from './components/bulletinMainView/BulletinMainViewBulletinBlock';
import BulletinMainViewFloatingButton from './components/bulletinMainView/BulletinMainViewFloatingButton';

interface BulletinMainViewProps {
  data: any;
}

interface BulletinMainViewStates {
  refreshing: boolean;
  categoryIdList: number[];
  mainCategory: string;
  page: number;
  data: any[];
}

export default class BulletinMainView extends React.Component<
  BulletinMainViewProps,
  BulletinMainViewStates
> {
  constructor(props: any) {
    super(props);
    this.state = {refreshing: false, categoryIdList: [], page: 0, data: []};
    this.getBulletinListApi();
  }

  private async getBulletinListApi() {
    try {
      const res = await BulletinApiController.getSearchBulletin({
        categoryIdList: this.state.categoryIdList,
        page: this.state.page ? this.state.page : 0,
        size: 10,
      });

      if (this.state.page === 0) {
        this.setState({data: []});
      }
      this.setState({
        data:
          this.state.page === 0
            ? res.data.content
            : [...this.state.data, ...res.data.content],
        refreshing: false,
      });
    } catch (e) {
      console.error(e);
      // alert('게시글 로드에 실패했습니다.');
    }
  }

  private onSelectMainCategory(main: string) {
    if (this.state.mainCategory === main) {
      this.setState({categoryIdList: [], mainCategory: '', page: 0}, () => {
        this.getBulletinListApi();
      });
      return;
    }

    let subList = Category.getSubCategoryList(main).map(item => +item.id);
    this.setState(
      {categoryIdList: subList, mainCategory: main, page: 0},
      () => {
        this.getBulletinListApi();
      },
    );
  }

  private LoadMoreBulletin() {
    this.setState({page: this.state.page + 1}, () => {
      this.getBulletinListApi();
    });
  }

  private toggleFavoriteBulletin(item: any) {
    const bulletinList = this.state.data;
    let updatedList = bulletinList.map(bulletin => {
      if (bulletin === item) {
        bulletin.myFavorite = !item.myFavorite;
      }
      return bulletin;
    });
    this.setState({data: updatedList});
  }

  private async postFavoriteBulletin(item: any) {
    try {
      await FavoriteApiController.postFavoriteBulletin({
        bulletinId: item.bulletinId,
      });
      this.toggleFavoriteBulletin(item);
    } catch (e) {
      console.error(e);
    }
  }

  private async deleteFavoriteBulletin(item: any) {
    try {
      await FavoriteApiController.deleteFavoriteBulletin({
        bulletinId: item.bulletinId,
      });
      this.toggleFavoriteBulletin(item);
    } catch (e) {
      console.error(e);
    }
  }

  render() {
    return (
      <View style={{backgroundColor: '#FFF', flex: 1}}>
        <BulletinMainViewHeader navigation={this.props.navigation} />
        <BulletinMainViewMatchingButton navigation={this.props.navigation} />

        <View>
          <ScrollView
            horizontal={true}
            showsVerticalScrollIndicator={false}
            showsHorizontalScrollIndicator={false}
            style={{marginLeft: 20}}>
            {Category.getCategoryList().map(main => (
              <BulletinMainViewCategoryButton
                mainCategory={this.state.mainCategory}
                title={main}
                onPress={(title: string) => this.onSelectMainCategory(title)}
              />
            ))}
          </ScrollView>
        </View>
        <View style={{backgroundColor: '#FFF', flex: 1}}>
          <FlatList
            style={this.styles.list}
            contentContainerStyle={this.styles.listContainer}
            columnWrapperStyle={{justifyContent: 'space-between'}}
            showsVerticalScrollIndicator={false}
            showsHorizontalScrollIndicator={false}
            data={this.state.data}
            extraData={this.state}
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
                <BulletinMainViewBulletinBlock
                  navigation={this.props.navigation}
                  bulletinInfo={item}
                  refreshParent={() => this.getBulletinListApi()}
                  postFavorite={(bulletinInfo: any) =>
                    this.postFavoriteBulletin(bulletinInfo)
                  }
                  deleteFavorite={(bulletinInfo: any) =>
                    this.deleteFavoriteBulletin(bulletinInfo)
                  }
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

        <BulletinMainViewFloatingButton navigation={this.props.navigation} />
      </View>
    );
  }

  private styles = StyleSheet.create({
    list: {
      paddingHorizontal: 10,
    },
    listContainer: {
      marginHorizontal: 20,
      marginTop: 20,
    },
    separator: {
      marginTop: 10,
    },
  });
}
