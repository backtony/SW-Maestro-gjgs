import React from 'react';
import {StyleSheet, View, Alert, FlatList} from 'react-native';
import FavoriteApiController from '../../../../services/apis/FavoriteApiController';
import UserDC from '../../../../services/login/UserDC';
import BulletinBlock from './BulletinBlock';

interface BulletinTabProps {
  navigation: any;
}

interface BulletinTabStates {
  data: any;
  refreshing: boolean;
}

export default class BulletinTab extends React.Component<
  BulletinTabProps,
  BulletinTabStates
> {
  constructor(props: any) {
    super(props);
    this.state = {data: [], refreshing: false};
  }

  componentDidMount() {
    this.getBulletin();
  }

  private async getBulletin() {
    if (UserDC.isLogout()) {
      return;
    }
    try {
      const res = await FavoriteApiController.getBulletin({});

      this.setState({
        data: res.data.favoriteBulletinDtoList,
      });
    } catch (e) {
      console.error(e);
      // Alert.alert('게시글 로드에 실패했습니다.');
    }
  }

  render() {
    return (
      <View style={this.styles.container}>
        <View style={this.styles.container}>
          <FlatList
            style={this.styles.list}
            contentContainerStyle={this.styles.listContainer}
            columnWrapperStyle={this.styles.columnWrapper}
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
                <BulletinBlock
                  navigation={this.props.navigation}
                  refreshParent={() => this.getBulletin()}
                  bulletin={item}
                />
              );
            }}
            refreshing={this.state.refreshing}
            onRefresh={() => {
              this.getBulletin();
            }}
          />
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    container: {backgroundColor: '#FFF', flex: 1},
    columnWrapper: {justifyContent: 'space-between'},
    list: {
      paddingHorizontal: 10,
    },

    separator: {
      marginTop: 10,
    },

    listContainer: {
      marginHorizontal: 20,
      marginTop: 20,
    },
  });
}
